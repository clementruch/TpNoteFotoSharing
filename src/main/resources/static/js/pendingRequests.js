// Fonction pour mettre à jour le compteur des demandes en attente
function updatePendingRequestsCount() {
    fetch('/api/contacts/pending-count')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(count => {
            const badge = document.getElementById('pendingRequestsBadge');
            if (count > 0) {
                badge.textContent = count;
                badge.style.display = 'inline';
            } else {
                badge.style.display = 'none';
            }
        })
        .catch(error => {
            console.error('Error fetching pending requests count:', error);
        });
}

// Fonction pour initialiser les événements et mettre à jour le compteur
function initializePendingRequestsUpdater() {
    updatePendingRequestsCount();

    setInterval(updatePendingRequestsCount, 1000);
}

// Initialiser la mise à jour du compteur au chargement de la page
document.addEventListener('DOMContentLoaded', initializePendingRequestsUpdater);
