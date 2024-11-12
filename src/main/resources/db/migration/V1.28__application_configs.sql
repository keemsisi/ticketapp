INSERT INTO application_config (name, data)
VALUES
    (
        'Standard Plan',
        '{
            "plan": "Standard",
            "platformFee": [
                {
                    "processor": "PayStack",
                    "feeType": "PERCENTAGE",
                    "amount": "3.5",
                    "currency": "NGN"
                }
            ],
            "paymentProcessingFee": [
                {
                    "processor": "PayStack",
                    "currency": "NGN",
                    "amount": "1.5",
                    "type": "PERCENTAGE"
                },
                {
                    "processor": "PayStack",
                    "currency": "foreign",
                    "amount": "3.9",
                    "type": "PERCENTAGE"
                }
            ],
            "additionalFee": [
                {
                    "processor": "PayStack",
                    "feeType": "PERCENTAGE",
                    "amount": "5",
                    "additionalFeeType": "FIXED",
                    "additionalAmount": "100",
                    "currency": "NGN"
                },
                {
                    "processor": "PayStack",
                    "feeType": "PERCENTAGE",
                    "amount": "5",
                    "additionalFeeType": "FIXED",
                    "additionalAmount": "100",
                    "currency": "foreign"
                }
            ],
            "tax": [
                {
                    "processor": "PayStack",
                    "currency": "NGN",
                    "value": "7.5",
                    "type": "PERCENTAGE"
                }
            ],
            "cappedAmount": [
                {
                    "processor": "PayStack",
                    "currency": "NGN",
                    "value": "2000",
                    "percentage": null
                }
            ],
            "features": [
                "Event listing",
                "Custom ticket types (General, VIP)",
                "Basic analytics (ticket sales)",
                "Email notifications",
                "Email support"
            ],
            "support": [
                "Basic Email Support"
            ],
            "subscriptionOption": [
                {
                    "currency": "NGN",
                    "amount": "0",
                    "frequency": "MONTHLY"
                }
            ]
        }'
    ),
    (
        'Pro Plan',
        '{
            "plan": "Pro",
            "platformFee": [
                {
                    "processor": "PayStack",
                    "feeType": "PERCENTAGE",
                    "amount": "3",
                    "currency": "NGN"
                }
            ],
            "paymentProcessingFee": [
                {
                    "processor": "PayStack",
                    "currency": "NGN",
                    "amount": "1.5",
                    "type": "PERCENTAGE"
                },
                {
                    "processor": "PayStack",
                    "currency": "foreign",
                    "amount": "3.9",
                    "type": "PERCENTAGE"
                }
            ],
            "additionalFee": [
                {
                    "processor": "PayStack",
                    "feeType": "PERCENTAGE",
                    "amount": "4.5",
                    "additionalFeeType": "FIXED",
                    "additionalAmount": "100",
                    "currency": "NGN"
                },
                {
                    "processor": "PayStack",
                    "feeType": "PERCENTAGE",
                    "amount": "4.5",
                    "additionalFeeType": "FIXED",
                    "additionalAmount": "100",
                    "currency": "foreign"
                }
            ],
            "tax": [
                {
                    "processor": "PayStack",
                    "currency": "NGN",
                    "value": "7.5",
                    "type": "PERCENTAGE"
                }
            ],
            "cappedAmount": [
                {
                    "processor": "PayStack",
                    "currency": "NGN",
                    "value": "3000",
                    "percentage": null
                }
            ],
            "features": [
                "All Standard features",
                "Advanced analytics (demographics, sales trends)",
                "Custom branding on ticket pages",
                "Event reminders and email marketing tools",
                "Early payouts for event organizers"
            ],
            "support": [
                "Priority Email & Phone Support"
            ],
            "subscriptionOption": [
                {
                    "currency": "NGN",
                    "amount": "20000",
                    "frequency": "MONTHLY"
                }
            ]
        }'
    ),
    (
        'Premium Plan',
        '{
            "plan": "Premium",
            "platformFee": [
                {
                    "processor": "PayStack",
                    "feeType": "PERCENTAGE",
                    "amount": "2.5",
                    "currency": "NGN"
                }
            ],
            "paymentProcessingFee": [
                {
                    "processor": "PayStack",
                    "currency": "NGN",
                    "amount": "1.5",
                    "type": "PERCENTAGE"
                },
                {
                    "processor": "PayStack",
                    "currency": "foreign",
                    "amount": "3.9",
                    "type": "PERCENTAGE"
                }
            ],
            "additionalFee": [
                {
                    "processor": "PayStack",
                    "feeType": "PERCENTAGE",
                    "amount": "4",
                    "additionalFeeType": "FIXED",
                    "additionalAmount": "100",
                    "currency": "NGN"
                },
                {
                    "processor": "PayStack",
                    "feeType": "PERCENTAGE",
                    "amount": "4",
                    "additionalFeeType": "FIXED",
                    "additionalAmount": "100",
                    "currency": "foreign"
                }
            ],
            "tax": [
                {
                    "processor": "PayStack",
                    "currency": "NGN",
                    "value": "7.5",
                    "type": "PERCENTAGE"
                }
            ],
            "cappedAmount": [
                {
                    "processor": "PayStack",
                    "currency": "NGN",
                    "value": "5000",
                    "percentage": null
                }
            ],
            "features": [
                "All Pro features",
                "Marketing features",
                "Dedicated account manager",
                "Advanced security and fraud protection",
                "Custom reporting tools"
            ],
            "support": [
                "24/7 Premium Support (Dedicated Account Manager)"
            ],
            "subscriptionOption": [
                {
                    "currency": "NGN",
                    "amount": "50000",
                    "frequency": "MONTHLY"
                }
            ]
        }'
    );
