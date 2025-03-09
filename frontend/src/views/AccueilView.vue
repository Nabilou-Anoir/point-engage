<!-- src/views/AccueilView.vue -->
<template>
  <div class="accueil-container">
    <h2>Accueil Étudiant</h2>

    <!-- Sélection de l'étudiant -->
    <div class="form-group">
      <label>Étudiant <span class="required">*</span></label>
      <select v-model="selectionEtudiant" @change="updateStats">
        <option disabled value="">-- Sélectionnez un étudiant --</option>
        <option v-for="et in etudiants" :key="et.idEtudiant" :value="et.idEtudiant">
          {{ et.nom }} {{ et.prenom }}
        </option>
      </select>
    </div>

    <!-- Zone d'affichage des statistiques -->
    <div v-if="statsDisponible" class="stats-section">
      <h3>Bonjour, {{ nomEtudiantSelectionne }} !</h3>

      <div class="stats-cards">
        <div class="stat-card">
          <h4>Total de points obtenus</h4>
          <p>{{ totalPointsValides}}</p>
        </div>
        <div class="stat-card">
          <h4>Nombre de participations</h4>
          <p>{{ nombreParticipations }}</p>
        </div>
        <div class="stat-card">
          <h4>Fiche en cours de validation</h4>
          <p>{{ nbFichesEnCours }}</p>
        </div>
      </div>

      <p>
        Pour plus d'info,
        <router-link to="/dispositif" class="info-link">
          cliquez ici
        </router-link>
      </p>
    </div>
    <div v-else class="stats-section">
      <p>Aucun historique disponible.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'

const etudiants = ref([])
const selectionEtudiant = ref('')
const totalPointsValides = ref(0)
const nombreParticipations = ref(0)
const nbFichesEnCours = ref(0)
const nomEtudiantSelectionne = ref('')

// La condition pour afficher les statistiques (on affiche si un étudiant est sélectionné et qu'il y a au moins une participation)
const statsDisponible = computed(() => {
  return selectionEtudiant.value && nombreParticipations.value > 0
})

// Endpoints API
const ETUDIANTS_URL = 'http://localhost:8989/api/etudiants'
const PARTICIPES_SEARCH_URL = 'http://localhost:8989/api/participes/search/findByIdIdEtudiant'

// Fonction pour charger la liste des étudiants
function getEtudiants() {
  fetch(ETUDIANTS_URL)
    .then(response => response.json())
    .then(data => {
      etudiants.value = data
    })
    .catch(error =>
      console.error("Erreur lors du chargement des étudiants :", error)
    )
}

// Fonction pour mettre à jour les statistiques pour l'étudiant sélectionné
function updateStats() {
  const id = selectionEtudiant.value
  // Récupérer l'objet étudiant sélectionné pour afficher son prénom
  const etu = etudiants.value.find(e => e.idEtudiant == id)
  nomEtudiantSelectionne.value = etu ? etu.prenom : ''

  // Construire l'URL pour récupérer les participations de l'étudiant
  const url = `${PARTICIPES_SEARCH_URL}?idEtudiant=${id}`

  fetch(url)
    .then(response => response.json())
    .then(data => {
      // Récupérer le tableau des participations (s'il existe)
      const participations = data._embedded?.participes || []
      let sumPoints = 0
      let countEnCours = 0

      participations.forEach(item => {
        // Si le statut est true et qu'il y a des points, on les ajoute
        if (item.statut === true && item.totalPoints) {
          sumPoints += item.totalPoints
        }
        // Si le statut est null, on compte la fiche comme en cours
        if (item.statut === null) {
          countEnCours++
        }
      })

      totalPointsValides.value = sumPoints
      nombreParticipations.value = participations.length
      nbFichesEnCours.value = countEnCours
    })
    .catch(error =>
      console.error("Erreur lors du chargement des participations :", error)
    )
}

onMounted(() => {
  getEtudiants()
})
</script>

<style scoped>
.accueil-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.form-group {
  margin-bottom: 1rem;
}

.required {
  color: red;
}

.stats-section {
  margin-top: 1.5rem;
}

.stats-cards {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.stat-card {
  flex: 1;
  background-color: #f7f7f7;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1rem;
  text-align: center;
}

.stat-card h4 {
  margin-bottom: 0.5rem;
  color: #6C3EA9;
}

.stat-card p {
  font-size: 1.2rem;
  font-weight: bold;
  margin: 0;
}

.info-link {
  color: #6C3EA9;
  text-decoration: underline;
  cursor: pointer;
}
</style>
