import { Component } from "@angular/core"
import { CommonModule } from "@angular/common"
import { FormBuilder,  FormGroup, ReactiveFormsModule } from "@angular/forms"

interface Transaction {
    date: string
    description: string
    amount: string
}

@Component({
    selector: "app-dashboard",
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: "./dashboard.component.html",
    styleUrls: ["./dashboard.component.scss"],
})
export class DashboardComponent {
    transferForm: FormGroup

    transactions: Transaction[] = [
        { date: "05/05/204", description: "Deposit", amount: "+$10.00" },
        { date: "05/04/204", description: "Online Transfer", amount: "$100.00" },
        { date: "05/05/204", description: "Grocery Store", amount: "-$20.00" },
        { date: "05/02/204", description: "Electricity Bill", amount: "-$15.00" },
        { date: "05/01/204", description: "Gym Membership", amount: "-$12.00" },
    ]

    constructor(private fb: FormBuilder) {
        this.transferForm = this.fb.group({
            recipient: [""],
            amount: ["100.00"],
        })
    }

    getAmountClass(amount: string): string {
        if (amount.startsWith("+")) {
            return "positive"
        } else if (amount.startsWith("-")) {
            return "negative"
        }
        return ""
    }

    onSubmit(): void {
        if (this.transferForm.valid) {
            console.log("Transfer submitted:", this.transferForm.value)
            // Implement transfer logic here
        }
    }
}
