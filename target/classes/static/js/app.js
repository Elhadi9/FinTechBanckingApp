// Banking App JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // Form validation
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const inputs = this.querySelectorAll('input[required]');
            let valid = true;

            inputs.forEach(input => {
                if (!input.value.trim()) {
                    valid = false;
                    input.classList.add('error');
                } else {
                    input.classList.remove('error');
                }
            });

            if (!valid) {
                e.preventDefault();
                alert('Please fill in all required fields');
            }
        });
    });

    // Amount validation
    const amountInputs = document.querySelectorAll('input[name="amount"]');
    amountInputs.forEach(input => {
        input.addEventListener('blur', function() {
            if (this.value && parseFloat(this.value) <= 0) {
                this.classList.add('error');
                alert('Amount must be greater than 0');
            } else {
                this.classList.remove('error');
            }
        });
    });

    // Transfer form validation
    const transferForm = document.querySelector('.transfer-form');
    if (transferForm) {
        transferForm.addEventListener('submit', function(e) {
            const fromAccount = this.querySelector('select[name="fromAccount"]');
            const toAccount = this.querySelector('input[name="toAccount"]');
            const amount = this.querySelector('input[name="amount"]');

            if (fromAccount.value === toAccount.value) {
                e.preventDefault();
                alert('Cannot transfer to the same account');
                return;
            }

            if (amount.value && parseFloat(amount.value) <= 0) {
                e.preventDefault();
                alert('Amount must be greater than 0');
            }
        });
    }

    // Auto-format currency values
    const balanceElements = document.querySelectorAll('.account-card p');
    balanceElements.forEach(element => {
        const text = element.textContent;
        if (text.includes('Balance:')) {
            // You could add currency formatting logic here
        }
    });
});