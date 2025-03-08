<template>
  <div>
    <h1>Historique des fiches</h1>

    <!-- Sélection de l'étudiant -->
    <div class="form-group">
      <label>Étudiant <span class="required">*</span></label>
      <select v-model="selectionEtudiant" required @change="getHistorique">
        <option disabled value="">-- Sélectionnez votre nom et prénom --</option>
        <option v-for="et in etudiants" :key="et.idEtudiant" :value="et.idEtudiant">
          {{ et.nom }} {{ et.prenom }}
        </option>
      </select>
    </div>

    <!-- Affichage de l'historique -->
    <div v-if="historique.length">
      <h2>Historique de l'étudiant</h2>
      <ul>
        <li
          v-for="item in historique"
          :key="`${item.id.idEtudiant}-${item.id.idAction}-${item.id.idSemestre}`"
        >
          Action : {{ item.action ? item.action.nom : 'N/A' }} -
          Semestre : {{ item.semestre ? item.semestre.nbSemestre : 'N/A' }} -
          Points : {{ item.totalPoints || 0 }} -
          Statut :
          <span v-if="item.statut === true">Validé</span>
          <span v-else-if="item.statut === false">Refusé</span>
          <span v-else>En attente</span>
        </li>
      </ul>
    </div>
    <div v-else>
      <p>Aucun historique disponible.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const etudiants = ref([])
const selectionEtudiant = ref('')
const historique = ref([])

// Endpoints API de base
const ETUDIANTS_URL = 'http://localhost:8989/api/etudiants'
const PARTICIPES_SEARCH_URL = 'http://localhost:8989/api/participes/search/findByIdIdEtudiant'
const ACTIONS_URL = 'http://localhost:8989/api/actions'
const SEMESTRES_URL = 'http://localhost:8989/api/semestres'

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

// Fonction pour charger l'historique des participations pour l'étudiant sélectionné
function getHistorique() {
  if (!selectionEtudiant.value) return

  const url = `${PARTICIPES_SEARCH_URL}?idEtudiant=${selectionEtudiant.value}`

  fetch(url)
    .then(response => {
      if (!response.ok) throw new Error(`Erreur ${response.status}`)
      return response.json()
    })
    .then(data => {
      if (data._embedded && data._embedded.participes) {
        const participations = data._embedded.participes
        // Pour chaque participation, on va récupérer l'action et le semestre via leurs identifiants
        const promises = participations.map(item => {
          // On construit l'URL pour récupérer l'action
          const actionUrl = `${ACTIONS_URL}/${item.id.idAction}`
          // Et l'URL pour récupérer le semestre
          const semestreUrl = `${SEMESTRES_URL}/${item.id.idSemestre}`

          return Promise.all([
            fetch(actionUrl).then(r => r.json()).catch(err => {
              console.error("Erreur lors du chargement de l'action :", err)
              return null
            }),
            fetch(semestreUrl).then(r => r.json()).catch(err => {
              console.error("Erreur lors du chargement du semestre :", err)
              return null
            })
          ]).then(([actionData, semestreData]) => {
            item.action = actionData
            item.semestre = semestreData
            return item
          })
        })
        Promise.all(promises).then(results => {
          historique.value = results
        })
      } else {
        historique.value = []
      }
    })
    .catch(error =>
      console.error("Erreur lors du chargement de l'historique :", error)
    )
}

onMounted(() => {
  getEtudiants()
})
</script>

<style scoped>
.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.4rem;
  font-weight: bold;
  color: #333;
}

.form-group select {
  width: 100%;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
  box-sizing: border-box;
  font: inherit;
}

.required {
  color: red;
}
</style>
