<template>
  <div class="activities-container">
    <h1>Historique des fiches</h1>

    <!-- Affichage du nom de l'étudiant connecté -->
    <p v-if="etudiant">
      <strong>Étudiant connecté :</strong> {{ etudiant.nom }} {{ etudiant.prenom }}
    </p>

    <!-- Barre de recherche -->
    <div class="search-filter">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="Rechercher par nom d'activité"
        class="search-input"
      />
    </div>

    <!-- Tableau de l'historique (avec pagination) -->
    <div class="table-container" v-if="paginatedHistorique.length > 0">
      <table class="activities-table">
        <thead>
        <tr>
          <th>Activité</th>
          <th>Date de Réalisation</th>
          <th class="text-right">Statut</th>
          <th class="text-right">Points</th>
        </tr>
        </thead>
        <tbody>
        <tr
          v-for="(item, index) in paginatedHistorique"
          :key="`${item.id.idEtudiant}-${item.id.idAction}-${item.id.idSemestre}`"
          class="table-row"
        >
          <td @click="showPopup(item)" class="activity-name">
            {{ item.action ? item.action.nom : 'N/A' }}
          </td>
          <td>{{ formatDate(item.dateDebutParticipation) }}</td>
          <td class="text-right">
            <span v-if="item.statut === true" class="status-valid">Validé</span>
            <span v-else-if="item.statut === false" class="status-pending">Refusé</span>
            <span v-else class="status-default">En attente</span>
          </td>
          <td class="text-right">
            <span class="points">{{ item.totalPoints || 0 }} pts</span>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div v-else>
      <p>Aucun historique disponible.</p>
    </div>

    <!-- Pagination -->
    <div class="pagination" v-if="totalPages > 1">
      <button
        @click="prevPage"
        :disabled="currentPage === 1"
        class="pagination-button"
      >
        Précédent
      </button>
      <span class="page-indicator">Page {{ currentPage }} sur {{ totalPages }}</span>
      <button
        @click="nextPage"
        :disabled="currentPage === totalPages"
        class="pagination-button"
      >
        Suivant
      </button>
    </div>

    <!-- Popup de détail -->
    <div v-if="selectedParticipation" class="modal-overlay" @click="closePopup">
      <div class="modal-content" @click.stop>
        <h2>
          {{ selectedParticipation.action ? selectedParticipation.action.nom : 'Détails de la fiche' }}
        </h2>
        <p><strong>Date de Réalisation:</strong> {{ formatDate(selectedParticipation.dateDebutParticipation) }}</p>
        <p>
          <strong>Statut:</strong>
          <span v-if="selectedParticipation.statut === true">Validé</span>
          <span v-else-if="selectedParticipation.statut === false">Refusé</span>
          <span v-else>En attente</span>
        </p>
        <p><strong>Points attribués:</strong> {{ selectedParticipation.totalPoints || 0 }}</p>
        <!-- Description de l'action, si présente -->
        <p v-if="selectedParticipation.action && selectedParticipation.action.description">
          <strong>Description de l'action:</strong> {{ selectedParticipation.action.description }}
        </p>
        <!-- Description de la participation, si présente -->
        <p v-if="selectedParticipation.descriptionParticipation">
          <strong>Description de la participation:</strong> {{ selectedParticipation.descriptionParticipation }}
        </p>
        <button @click="closePopup" class="modal-close-button">Fermer</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'

// États réactifs pour l'historique et l'étudiant
const historique = ref([])
const etudiant = ref(null)

// Récupération de l'utilisateur connecté depuis le sessionStorage
const loggedInUser = ref(null)
if (sessionStorage.getItem('loggedInUser')) {
  loggedInUser.value = JSON.parse(sessionStorage.getItem('loggedInUser'))
} else {
  console.log('Aucun utilisateur connecté trouvé dans le sessionStorage.')
}

// Endpoints API
const ETUDIANT_BY_EMAIL_URL = 'http://localhost:8989/api/etudiants/byEmail?email='
const PARTICIPES_SEARCH_URL = 'http://localhost:8989/api/participes/search/findByIdIdEtudiant'
const ACTIONS_URL = 'http://localhost:8989/api/actions'
const SEMESTRES_URL = 'http://localhost:8989/api/semestres'

// États pour la recherche, la pagination et le popup
const searchQuery = ref('')
const currentPage = ref(1)
const itemsPerPage = ref(5)
const selectedParticipation = ref(null)

// Fonction pour charger l'historique de l'étudiant connecté
async function getHistorique() {
  try {
    if (!loggedInUser.value || !loggedInUser.value.email) {
      console.log("L'utilisateur connecté n'a pas d'email.")
      return
    }
    // 1) Récupérer l'étudiant via son email
    const etuResponse = await fetch(ETUDIANT_BY_EMAIL_URL + encodeURIComponent(loggedInUser.value.email))
    if (!etuResponse.ok) {
      console.log('Erreur lors de la récupération de l’étudiant :', etuResponse.statusText)
      return
    }
    const etudiantData = await etuResponse.json()
    console.log("Données de l’étudiant récupérées :", etudiantData)
    if (!etudiantData.idEtudiant) {
      console.log("Aucun idEtudiant trouvé pour l'utilisateur connecté (", loggedInUser.value.email, ")")
      return
    }
    etudiant.value = etudiantData

    // 2) Récupérer les participations à partir de l'idEtudiant
    const url = `${PARTICIPES_SEARCH_URL}?idEtudiant=${etudiantData.idEtudiant}`
    console.log("URL de recherche des participations :", url)
    const partResponse = await fetch(url)
    if (!partResponse.ok) {
      console.log('Erreur lors de la récupération des participations :', partResponse.statusText)
      return
    }
    const partData = await partResponse.json()
    console.log("Données brutes des participations :", partData)
    const participations = partData._embedded && partData._embedded.participes
      ? partData._embedded.participes
      : []
    if (participations.length === 0) {
      console.log("Aucune participation trouvée pour cet étudiant.")
    }

    // 3) Pour chaque participation, charger les détails de l'action et du semestre
    const fetchDetails = participations.map(async (item) => {
      try {
        const actionRes = await fetch(`${ACTIONS_URL}/${item.id.idAction}`)
        if (actionRes.ok) {
          item.action = await actionRes.json()
        } else {
          console.log('Erreur lors de la récupération de l\'action:', actionRes.statusText)
        }
        const semestreRes = await fetch(`${SEMESTRES_URL}/${item.id.idSemestre}`)
        if (semestreRes.ok) {
          item.semestre = await semestreRes.json()
        } else {
          console.log('Erreur lors de la récupération du semestre:', semestreRes.statusText)
        }
      } catch (err) {
        console.error('❌ Erreur chargement des détails pour une participation :', err)
      }
      return item
    })
    const results = await Promise.all(fetchDetails)
    console.log("Participations avec détails :", results)
    historique.value = results
  } catch (error) {
    console.error("Erreur lors du chargement de l'historique :", error)
  }
}

// Fonction pour formater une date
function formatDate(dateStr) {
  if (!dateStr) return 'N/A'
  const d = new Date(dateStr)
  return d.toLocaleDateString('fr-FR')
}

onMounted(getHistorique)

// Filtrage des participations par nom d'activité
const filteredHistorique = computed(() => {
  if (!searchQuery.value.trim()) return historique.value
  return historique.value.filter(item => {
    const activityName = item.action && item.action.nom ? item.action.nom.toLowerCase() : ''
    return activityName.includes(searchQuery.value.toLowerCase())
  })
})

// Pagination
const totalPages = computed(() => {
  return Math.ceil(filteredHistorique.value.length / itemsPerPage.value)
})

const paginatedHistorique = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value
  return filteredHistorique.value.slice(start, start + itemsPerPage.value)
})

function nextPage() {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  }
}

function prevPage() {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

// Popup pour afficher le détail d'une participation
function showPopup(item) {
  selectedParticipation.value = item
}

function closePopup() {
  selectedParticipation.value = null
}
</script>

<style scoped>
/* Styles inspirés du composant ActivitiesView */
.activities-container {
  max-width: 1300px;
  margin: 40px auto;
  padding: 40px;
  background: #ffffff;
  border-radius: 20px;
  box-shadow: 0 15px 40px rgba(217, 199, 199, 0.12);
  animation: fadeIn 0.5s ease-in-out;
}

.search-filter {
  margin-bottom: 30px;
}

.search-input {
  width: 30%;
  padding: 12px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 7px;
  font-size: 1rem;
  color: #333;
  background: #f9f9f9;
  transition: all 0.3s ease;
}

.search-input:focus {
  border-color: #6A3FA0;
  box-shadow: 0 0 12px rgba(106, 63, 160, 0.3);
  outline: none;
}

.table-container {
  overflow-x: auto;
  border-radius: 7px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.activities-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.activities-table th,
.activities-table td {
  padding: 16px 20px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.activities-table th {
  color: #666;
  font-weight: 600;
  text-transform: uppercase;
  font-size: 0.7rem;
}

.text-right {
  text-align: right;
}

.activities-table tbody tr {
  transition: background-color 0.3s ease;
}

.activities-table tbody tr:hover {
  background-color: #f8f8f8;
}

.activity-name {
  cursor: pointer;
  color: #6A3FA0;
  font-weight: 500;
  text-decoration: underline;
}

.points,
.status-valid,
.status-pending,
.status-default {
  padding: 6px 12px;
  border-radius: 7px;
  font-weight: 500;
}

.points,
.status-valid {
  color: #4CAF50;
  background-color: #e8f5e9;
}

.status-pending {
  color: #FFA000;
  background-color: #fff3e0;
}

.status-default {
  color: #333;
  background-color: #f5f5f5;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-top: 30px;
}

.pagination-button {
  padding: 10px 20px;
  border: none;
  border-radius: 12px;
  background-color: #6A3FA0;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.pagination-button:disabled {
  background-color: #e0e0e0;
  cursor: not-allowed;
}

.pagination-button:hover:not(:disabled) {
  background-color: #5a2f8f;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(106, 63, 160, 0.3);
}

.page-indicator {
  font-size: 1rem;
  color: #333;
}

/* Popup */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(5px);
  animation: fadeIn 0.3s ease-in-out;
}

.modal-content {
  background: white;
  padding: 40px;
  border-radius: 16px;
  max-width: 900px;
  width: 90%;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  transform: scale(0.95);
  opacity: 0;
  animation: popIn 0.3s ease-in-out forwards;
}

@keyframes popIn {
  to {
    transform: scale(1);
    opacity: 1;
  }
}

.modal-content h2 {
  margin-bottom: 20px;
  color: #6A3FA0;
  font-size: 1.8rem;
  font-weight: 700;
}

.modal-content p {
  margin-bottom: 15px;
  font-size: 1rem;
  color: #555;
}

.modal-close-button {
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  background-color: #6A3FA0;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 30px;
  font-size: 1rem;
  box-shadow: 0 4px 10px rgba(106, 63, 160, 0.3);
}

.modal-close-button:hover {
  background-color: #5a2f8f;
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(106, 63, 160, 0.4);
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
