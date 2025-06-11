package org.ebanking.service.Impl;

import jakarta.transaction.Transactional;
import org.ebanking.dao.AccountRepository;
import org.ebanking.dao.TransactionRepository;
import org.ebanking.dto.request.TransferRequest;
import org.ebanking.dto.response.TransferResponse;
import org.ebanking.model.*;
import org.ebanking.model.enums.AccountType;
import org.ebanking.model.enums.TransactionStatus;
import org.ebanking.service.AccountService;
import org.ebanking.service.TransferService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

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
    public TransferResponse processTransfer(Long ownerId, TransferRequest request) {
        // 1. Récupérer le compte source du client
        CurrentAccount sourceAccount = accountRepository.findCurrentAccountByOwnerId(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Aucun compte courant trouvé pour ce client"));

        // 2. Vérifier le solde avec découvert
        //validateBalanceWithOverdraft(sourceAccount, request.getAmount());

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


    @Override
    public TransferResponse processTransferFromSavings(Long ownerId, String savingsAccountNumber, TransferRequest request) {
        // Get the savings account directly without casting
        SavingsAccount savingsAccount = accountRepository.findSavingsAccountByOwnerId(ownerId)
                .filter(a -> a.getAccountNumber().equals(savingsAccountNumber))
                .orElseThrow(() -> new IllegalArgumentException("Invalid savings account"));

        CurrentAccount currentAccount = accountRepository.findCurrentAccountByOwnerId(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("No current account found"));

        validateTransferFromSavings(savingsAccount, currentAccount, request.getAmount());

        Transfer transfer = createTransfer(savingsAccount, request.getAmount(), currentAccount.getAccountNumber());

        savingsAccount.debit(request.getAmount());
        currentAccount.credit(request.getAmount());

        accountRepository.saveAll(List.of(savingsAccount, currentAccount));
        transactionRepository.save(transfer);

        return buildResponse(transfer);
    }

    private Transfer createTransfer(Account source, BigDecimal amount, String destinationAccount) {
        Transfer transfer = new Transfer();
        transfer.setAccount(source);
        transfer.setAmount(amount);
        transfer.setDestinationAccount(destinationAccount);
        transfer.setStatus(TransactionStatus.COMPLETED);
        transfer.setTransactionDate(OffsetDateTime.now());
        transfer.setReference(generateReference());
        return transfer;
    }

    @Override
    public String findClientSavingsAccount(Long clientId) {
        return accountRepository.findSavingsAccountByOwnerId(clientId)
                .map(Account::getAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Client has no savings account"));
    }

    @Override
    public String findClientCurrentAccount(Long clientId) {
        return accountRepository.findCurrentAccountByOwnerId(clientId)
                .map(Account::getAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Client has no current account"));
    }

    private void validateTransfer(CurrentAccount source, Account destination, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }



        if (source.getAccountNumber().equals(destination.getAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
    }

    private void validateTransferFromSavings(SavingsAccount source, CurrentAccount destination, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }


        if (source.getAccountNumber().equals(destination.getAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
    }


    private void validateBalanceWithOverdraft(CurrentAccount account, BigDecimal amount) {
        BigDecimal availableBalance = account.getBalance().add(account.getOverdraftLimit());
        if (availableBalance.compareTo(amount) < 0) {
            throw new IllegalStateException("Solde insuffisant. Découvert maximum: " + account.getOverdraftLimit());
        }
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

    private Transfer createTransferFromSavingsEntity(Account sourceAccount, TransferRequest request) {
        Transfer transfer = new Transfer();
        transfer.setAccount(sourceAccount);
        transfer.setAmount(request.getAmount());
        transfer.setDestinationAccount(request.getDestinationAccount());
        transfer.setStatus(TransactionStatus.COMPLETED);
        transfer.setTransactionDate(OffsetDateTime.now());
        return transfer;
    }

    private TransferResponse buildResponse(Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                transfer.getReference(),
                transfer.getAmount(),
                transfer.getDestinationAccount(),
                transfer.getStatus(),
                transfer.getTransactionDate(),
                "Transfer completed successfully"
        );
    }

    private void validateTransfer(Account source, Account destination, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (source.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance");
        }

        if (source.getAccountNumber().equals(destination.getAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
    }



    private String generateReference() {
        return "TRF-" + System.currentTimeMillis();
    }

    @Override
    public TransferResponse transferToSavings(Long clientId, BigDecimal amount) {
        // 1. Validation du montant
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        // 2. Récupération des comptes
        CurrentAccount currentAccount = accountRepository.findCurrentAccountByOwnerId(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Aucun compte courant trouvé pour ce client"));

        SavingsAccount savingsAccount = accountRepository.findSavingsAccountByOwnerId(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Aucun compte épargne trouvé pour ce client"));


        // 4. Créer et sauvegarder le transfert
        Transfer transfer = createTransfer(currentAccount, amount, savingsAccount.getAccountNumber());
        transactionRepository.save(transfer);

        // 5. Débiter et créditer
        currentAccount.debit(amount);
        savingsAccount.credit(amount);

        // 6. Sauvegarder les comptes
        accountRepository.saveAll(List.of(currentAccount, savingsAccount));

        // 7. Préparer la réponse
        return buildResponse(transfer);
    }
}