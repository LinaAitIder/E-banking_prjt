package org.ebanking.service.Impl;

import jakarta.transaction.Transactional;
import org.ebanking.dao.AccountRepository;
import org.ebanking.dao.TransactionRepository;
import org.ebanking.dto.request.TransferRequest;
import org.ebanking.dto.response.TransferResponse;
import org.ebanking.model.*;
import org.ebanking.model.enums.TransactionStatus;
import org.ebanking.service.AccountService;
import org.ebanking.service.TransferService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@Transactional
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransferServiceImpl(AccountRepository accountRepository,
                               TransactionRepository transactionRepository,
                               AccountService accountService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }
    @Override
    public TransferResponse processTransfer(Long clientId, TransferRequest request) {
        // 1. Récupérer le compte source du client
        CurrentAccount sourceAccount = accountRepository.findCurrentAccountByOwnerId(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Aucun compte courant trouvé pour ce client"));

        // 2. Vérifier le solde avec découvert
        validateBalanceWithOverdraft(sourceAccount, request.getAmount());

        // verify if the destination acc exists
        if (!accountService.doesAccountExist(request.getDestinationAccount())) {
            throw new IllegalArgumentException("Le compte destinataire n'existe pas");
        }

        // get destination CURRENT account
        CurrentAccount destinationAccount = accountRepository.findByAccountNumber(request.getDestinationAccount())
                .orElseThrow(() -> new IllegalArgumentException("Le compte destinataire est introuvable"));

        // 3. Créer et sauvegarder le transfert
        Transfer transfer = createTransferEntity(sourceAccount, request);
        transactionRepository.save(transfer);

        // 4. debit & credit
        sourceAccount.debit(request.getAmount());
        destinationAccount.credit(request.getAmount());

        // 5. Préparer la réponse
        return new TransferResponse(
                transfer.getId(),
                transfer.getReference(),
                transfer.getAmount(),
                transfer.getDestinationAccount(),
                transfer.getStatus(),
                transfer.getTransactionDate(),
                "Transfert effectué avec succès"
        );
    }

    private Transfer createTransferEntity(CurrentAccount sourceAccount, TransferRequest request) {
        Transfer transfer = new Transfer();
        transfer.setAccount(sourceAccount);
        transfer.setAmount(request.getAmount());
        transfer.setDestinationAccount(request.getDestinationAccount());
        transfer.setStatus(TransactionStatus.COMPLETED);
        transfer.setTransactionDate(OffsetDateTime.now());
        transfer.setReference(generateReference());
        return transfer;
    }

    private void validateBalanceWithOverdraft(CurrentAccount account, BigDecimal amount) {
        BigDecimal availableBalance = account.getBalance().add(account.getOverdraftLimit());
        if (availableBalance.compareTo(amount) < 0) {
            throw new IllegalStateException("Solde insuffisant. Découvert maximum: " + account.getOverdraftLimit());
        }
    }

    private String generateReference() {
        return "TRF-" + System.currentTimeMillis();
    }
}