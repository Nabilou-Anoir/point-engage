<template>
  <div class="notifications-container">
    <h2>Notifications pour {{ selectedEtudiantName }}</h2>

    <!-- Liste des notifications -->
    <ul v-if="notifications.length">
      <li v-for="(notification, index) in notifications" :key="index">
        {{ notification.message }}
        <button @click="removeNotification(index)">X</button>
      </li>
    </ul>
    <p v-else>Aucune notification pour le moment.</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const notifications = ref([])
const selectedEtudiant = sessionStorage.getItem('selectedEtudiant') || ''
const selectedEtudiantName = sessionStorage.getItem('selectedEtudiantName') || 'Inconnu'

// Historique des participations
let previousParticipations = []

const PARTICIPES_SEARCH_URL = `http://localhost:8989/api/participes/search/findByIdIdEtudiant?idEtudiant=${selectedEtudiant}`

// Vérifier les participations et détecter les changements
async function checkParticipations() {
  if (!selectedEtudiant) return

  try {
    const response = await fetch(PARTICIPES_SEARCH_URL)
    const data = await response.json()
    const participations = data._embedded?.participes || []

    participations.forEach(participation => {
      const oldParticipation = previousParticipations.find(p => p.id.idAction === participation.id.idAction)

      if (oldParticipation) {
        // Détecter changement de statut
        if (oldParticipation.statut === null && participation.statut !== null) {
          const message = participation.statut
            ? "Votre action a été validée par le référent"
            : "Votre action a été refusée par le référent"
          addNotification(message)
        }

        // Détecter attribution de points
        if (oldParticipation.totalPoints !== participation.totalPoints) {
          addNotification(`Attribution de points : ${participation.totalPoints} points ajoutés`)
        }
      }
    })

    previousParticipations = participations
  } catch (error) {
    console.error("Erreur lors de la récupération des participations :", error)
  }
}

// Ajouter une notification à la liste
function addNotification(message) {
  notifications.value.push({ message })
}

// Supprimer une notification
function removeNotification(index) {
  notifications.value.splice(index, 1)
}

// Vérifier les participations toutes les 10 secondes
onMounted(() => {
  checkParticipations()
  setInterval(checkParticipations, 10000)
})

// Vérifier soumission de fiche
const ficheSoumise = sessionStorage.getItem("ficheSoumise")
if (ficheSoumise) {
  addNotification("Fiche soumise avec succès")
  sessionStorage.removeItem("ficheSoumise")
}
</script>

<style scoped>
.notifications-container {
  max-width: 600px;
  margin: 20px auto;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  background: #f9f9f9;
  margin: 8px 0;
  padding: 10px;
  border-radius: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

button {
  background: red;
  color: white;
  border: none;
  padding: 5px 8px;
  cursor: pointer;
  border-radius: 50%;
}
</style>
