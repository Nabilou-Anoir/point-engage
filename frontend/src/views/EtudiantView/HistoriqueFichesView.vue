<template>
  <div class="historique-container">
    <h1>Historique des fiches</h1>

    <!-- Sélection de l'étudiant -->
    <div class="form-group">
      <label>Étudiant <span class="required">*</span></label>
      <select v-model="selectionEtudiant" required @change="getHistorique">
        <option disabled value="">-- Sélectionnez votre nom et prénom --</option>
        <option
          v-for="et in etudiants"
          :key="et.idEtudiant"
          :value="et.idEtudiant"
        >
          {{ et.nom }} {{ et.prenom }}
        </option>
      </select>
    </div>

    <!-- Tableau d'historique -->
    <div v-if="historique.length">
      <table class="table-historique">
        <thead>
        <tr>
          <th>Activités</th>
          <th>Date de Réalisation</th>
          <th>Statut</th>
          <th>Points</th>
        </tr>
        </thead>
        <tbody>
        <tr
          v-for="item in historique"
          :key="`${item.id.idEtudiant}-${item.id.idAction}-${item.id.idSemestre}`"
        >
          <!-- Nom de l'action -->
          <td>
            {{ item.action ? item.action.nom : 'N/A' }}
          </td>

          <!-- Date de soumission (on choisit dateDebutParticipation) -->
          <td>
            <!-- Petite fonction formatDate si besoin, sinon brute -->
            {{ formatDate(item.dateDebutParticipation) }}
          </td>

          <!-- Statut -->
          <td>
              <span
                v-if="item.statut === true"
                class="statut valide"
              >
                Validé
              </span>
            <span
              v-else-if="item.statut === false"
              class="statut refuse"
            >
                Refusé
              </span>
            <span
              v-else
              class="statut attente"
            >
                En attente de validation
              </span>
          </td>

          <!-- Points -->
          <td>
            <!-- Si vous souhaitez un affichage comme "0.1 points attribués" -->
            {{ item.totalPoints || 0 }} points attribués
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div v-else>
      <p>Aucun historique disponible.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// États réactifs
const etudiants = ref([])
const selectionEtudiant = ref('')
const historique = ref([])

// Endpoints
const ETUDIANTS_URL = 'http://localhost:8989/api/etudiants'
const PARTICIPES_SEARCH_URL = 'http://localhost:8989/api/participes/search/findByIdIdEtudiant'
const ACTIONS_URL = 'http://localhost:8989/api/actions'
const SEMESTRES_URL = 'http://localhost:8989/api/semestres'

// Fonction pour charger la liste des étudiants
function getEtudiants() {
  fetch(ETUDIANTS_URL)
    .then(res => res.json())
    .then(data => {
      etudiants.value = data
    })
    .catch(err => {
      console.error('Erreur lors du chargement des étudiants :', err)
    })
}

// Fonction pour charger l'historique de l’étudiant sélectionné
function getHistorique() {
  // On ne fait rien si aucun étudiant n’est sélectionné
  if (!selectionEtudiant.value) return

  const url = `${PARTICIPES_SEARCH_URL}?idEtudiant=${selectionEtudiant.value}`
  fetch(url)
    .then(res => res.json())
    .then(data => {
      // Récupération du tableau de participations
      const participations = (data._embedded && data._embedded.participes) ? data._embedded.participes : []

      // Pour chaque participation, on va chercher l'action et le semestre
      const fetchDetails = participations.map(item => {
        const actionUrl = `${ACTIONS_URL}/${item.id.idAction}`
        const semestreUrl = `${SEMESTRES_URL}/${item.id.idSemestre}`

        // On enchaîne les fetchs pour action puis semestre
        return fetch(actionUrl)
          .then(r => r.json())
          .then(actionData => {
            item.action = actionData
            return fetch(semestreUrl)
          })
          .then(r => r.json())
          .then(semestreData => {
            item.semestre = semestreData
            return item
          })
          .catch(err => {
            console.error('Erreur lors du chargement des détails:', err)
            return item // On retourne quand même l'item pour éviter de casser la chaîne
          })
      })

      // Une fois tous les fetchs terminés, on met à jour l’historique
      return Promise.all(fetchDetails)
    })
    .then(results => {
      if (results) {
        historique.value = results
      }
    })
    .catch(err => {
      console.error('Erreur lors du chargement de l\'historique :', err)
    })
}

// Fonction simple pour formater la date si vous le souhaitez
function formatDate(dateStr) {
  if (!dateStr) return 'N/A'
  const d = new Date(dateStr)
  return d.toLocaleDateString('fr-FR') // ex: 01/03/2025
}

onMounted(() => {
  getEtudiants()
})
</script>

<style scoped>
.historique-container {
  max-width: 900px;
  margin: 0 auto;
}

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

/* Table */
.table-historique {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
  background-color: #fff;
}

.table-historique th,
.table-historique td {
  text-align: left;
  padding: 12px 16px;
  border-bottom: 1px solid #eee;
}

.table-historique th {
  background-color: #f5f5f5;
  font-weight: bold;
}

/* Statuts colorés */
.statut {
  padding: 4px 8px;
  border-radius: 4px;
  color: #fff;
  font-weight: 600;
  font-size: 0.9rem;
}

.statut.valide {
  background-color: #4caf50; /* vert */
}

.statut.refuse {
  background-color: #f44336; /* rouge */
}

.statut.attente {
  background-color: #ff9800; /* orange */
}
</style>
