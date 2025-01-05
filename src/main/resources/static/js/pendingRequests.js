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
                badge.textContent = count; // Mettre à jour le texte avec le nombre de demandes
                badge.style.display = 'inline'; // Afficher le badge si des demandes existent
            } else {
                badge.style.display = 'none'; // Cacher le badge si aucune demande
            }
        })
        .catch(error => {
            console.error('Error fetching pending requests count:', error);
        });
}

// Fonction pour initialiser les événements et mettre à jour le compteur
function initializePendingRequestsUpdater() {
    // Mettre à jour immédiatement lors du chargement de la page
    updatePendingRequestsCount();

    // Mettre à jour toutes les 10 secondes
    setInterval(updatePendingRequestsCount, 10000);
}

// Initialiser la mise à jour du compteur au chargement de la page
document.addEventListener('DOMContentLoaded', initializePendingRequestsUpdater);
