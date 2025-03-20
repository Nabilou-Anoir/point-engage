<template>
  <div class="notifications-container">
    <h2>Notifications </h2>

    <!-- Liste des notifications -->
    <ul v-if="notifications.length">
      <li v-for="(notification, index) in notifications" :key="index">
        <span :class="getNotificationClass(notification.type)">{{ notification.message }}</span>
        <button @click="removeNotification(index)">X</button>
      </li>
    </ul>
    <p v-else>Aucune notification pour le moment.</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watchEffect } from 'vue'

// Récupération de l'étudiant sélectionné depuis sessionStorage
const notifications = ref([])
const selectedEtudiant = sessionStorage.getItem('selectedEtudiant') || ''

// Historique des participations stocké sous forme de Map pour un accès rapide
let previousParticipations = new Map()

// Endpoint pour récupérer les participations de l'étudiant
const PARTICIPES_SEARCH_URL = `http://localhost:8989/api/participes/search/findByIdIdEtudiant?idEtudiant=${selectedEtudiant}`

// Vérifier les participations et détecter les changements
function checkParticipations() {
  if (!selectedEtudiant) return

  fetch(PARTICIPES_SEARCH_URL)
    .then(response => response.json())
    .then(data => {
      const participations = data._embedded?.participes || []

      participations.forEach(participation => {
        const key = `${participation.id.idEtudiant}-${participation.id.idAction}-${participation.id.idSemestre}`
        const oldParticipation = previousParticipations.get(key)

        if (oldParticipation) {
          // 🟢 Détecter changement de statut
          if (oldParticipation.statut === null && participation.statut !== null) {
            const message = participation.statut
              ? "✅ Votre action a été validée par le référent"
              : "❌ Votre action a été refusée par le référent"
            addNotification(message, participation.statut ? "success" : "error")
          }

          // 🏆 Détecter attribution de points
          if (oldParticipation.totalPoints !== participation.totalPoints) {
            addNotification(`🏆 Attribution de points : ${participation.totalPoints} points ajoutés`, "points")
          }
        }

        // Mettre à jour l'historique des participations
        previousParticipations.set(key, participation)
      })
    })
    .catch(() => {
      addNotification("⚠️ Erreur de chargement des notifications", "error")
    })
}


// Ajouter une notification à la liste avec suppression automatique
function addNotification(message, type = "info") {
  const notification = { message, type }
  notifications.value.push(notification)

  // Supprimer la notification après 15 secondes
  setTimeout(() => {
    const index = notifications.value.indexOf(notification)
    if (index !== -1) {
      notifications.value.splice(index, 1)
    }
  }, 15000)
}

// Supprimer manuellement une notification
function removeNotification(index) {
  notifications.value.splice(index, 1)
}

// Vérifier les participations toutes les 10 secondes
onMounted(() => {
  checkParticipations()
  setInterval(checkParticipations, 10000)
})

// 📄 Vérifier la soumission de fiche et ajouter une notification si nécessaire
watchEffect(() => {
  const ficheSoumise = sessionStorage.getItem("ficheSoumise")
  if (ficheSoumise) {
    addNotification("📄 Fiche soumise avec succès", "info")
    sessionStorage.removeItem("ficheSoumise") // Nettoyage après affichage
  }
})

// 🎨 Gestion des classes CSS pour les notifications
function getNotificationClass(type) {
  return {
    success: "notification-success",
    error: "notification-error",
    points: "notification-points",
    info: "notification-info"
  }[type]
}
</script>

<style scoped>
.notifications-container {
  max-width: 600px;
  margin: 20px auto;
}

/* Liste des notifications */
ul {
  list-style: none;
  padding: 0;
}

li {
  background: #f9f9f9;
  margin: 8px 0;
  padding: 12px;
  border-radius: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-left: 5px solid #2196F3; /* Bleu */
  animation: fadeIn 0.3s ease-in-out;
}

li:nth-child(odd) {
  background: #eef5ff;
}

/* Bouton de suppression */
button {
  background: red;
  color: white;
  border: none;
  padding: 5px 8px;
  cursor: pointer;
  border-radius: 50%;
  font-weight: bold;
}

/* 🎨 Styles des notifications */
.notification-success {
  color: #2e7d32;
  font-weight: bold;
}

.notification-error {
  color: #d32f2f;
  font-weight: bold;
}

.notification-points {
  color: #f9a825;
  font-weight: bold;
}

.notification-info {
  color: #1976d2;
  font-weight: bold;
}

/* Animation d'apparition */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-5px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
