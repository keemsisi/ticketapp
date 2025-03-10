INSERT INTO public.application_config
(id, date_created, date_modified, deleted,
"index", modified_by, tenant_id, user_id,
"version",
"data",
"name", version_id)
VALUES('4faf4837-70ed-46ad-866a-763b289d5532'::uuid, '2024-11-29 00:00:00.000', NULL,
false,
7, NULL, NULL, NULL,
0, '{
    "plan": "Standard",
    "platformFee": [
        {
            "processor": "PayStack",
            "type": "PERCENTAGE",
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
            "type": "FIXED",
            "amount": "100",
            "additionalAmount": "100",
            "currency": "NGN"
        },
        {
            "processor": "PayStack",
            "type": "FIXED",
            "amount": "100",
            "additionalAmount": "100",
            "currency": "foreign"
        }
    ],
    "tax": [
        {
            "processor": "PayStack",
            "currency": "NGN",
            "value": "0",
            "amount": "7.5",
            "type": "PERCENTAGE"
        }
    ],
    "cappedAmount": [
        {
            "processor": "PayStack",
            "currency": "NGN",
            "amount": "5000",
            "value": "2000",
            "type": "FIXED"
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
}', 'STANDARD_PLAN', NULL);
INSERT INTO public.application_config
(id, date_created, date_modified, deleted,
"index", modified_by, tenant_id, user_id,
"version",
"data",
"name", version_id)
VALUES('bcad5c8a-6a31-48a6-9ac2-52e1883c76ef'::uuid, '2024-11-29 00:00:00.000', NULL,
false,
8, NULL, NULL, NULL,
0, '{
    "plan": "Standard",
    "platformFee": [
        {
            "processor": "PayStack",
            "type": "PERCENTAGE",
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
            "type": "FIXED",
            "amount": "100",
            "additionalAmount": "100",
            "currency": "NGN"
        },
        {
            "processor": "PayStack",
            "type": "FIXED",
            "amount": "100",
            "additionalAmount": "100",
            "currency": "foreign"
        }
    ],
    "tax": [
        {
            "processor": "PayStack",
            "currency": "NGN",
            "value": "0",
            "amount": "7.5",
            "type": "PERCENTAGE"
        }
    ],
    "cappedAmount": [
        {
            "processor": "PayStack",
            "currency": "NGN",
            "amount": "5000",
            "value": "2000",
            "type": "FIXED"
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
}', 'PRO_PLAN', NULL);
INSERT INTO public.application_config
(id, date_created, date_modified, deleted,
"index", modified_by, tenant_id, user_id,
"version",
"data",
"name", version_id)
VALUES('490eb678-4a8a-45f6-b9a1-97e94cb17295'::uuid, '2024-11-29 00:00:00.000', NULL,
false,
9, NULL, NULL, NULL,
0, '{
    "plan": "Standard",
    "platformFee": [
        {
            "processor": "PayStack",
            "type": "PERCENTAGE",
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
            "type": "FIXED",
            "amount": "100",
            "additionalAmount": "100",
            "currency": "NGN"
        },
        {
            "processor": "PayStack",
            "type": "FIXED",
            "amount": "100",
            "additionalAmount": "100",
            "currency": "foreign"
        }
    ],
    "tax": [
        {
            "processor": "PayStack",
            "currency": "NGN",
            "value": "0",
            "amount": "7.5",
            "type": "PERCENTAGE"
        }
    ],
    "cappedAmount": [
        {
            "processor": "PayStack",
            "currency": "NGN",
            "amount": "5000",
            "value": "2000",
            "type": "FIXED"
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
}', 'PREMIUM_PLAN', NULL);