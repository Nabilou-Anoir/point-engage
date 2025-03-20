<template>
  <div class="historique-container">
    <h1>Historique des fiches</h1>

    <!-- Affichage du nom de l'étudiant sélectionné -->
    <p v-if="selectionEtudiantNom"><strong>Étudiant sélectionné :</strong> {{ selectionEtudiantNom }}</p>

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
        <tr v-for="item in historique" :key="`${item.id.idEtudiant}-${item.id.idAction}-${item.id.idSemestre}`">
          <td>{{ item.action ? item.action.nom : 'N/A' }}</td>
          <td>{{ formatDate(item.dateDebutParticipation) }}</td>
          <td>
            <span v-if="item.statut === true" class="statut valide">Validé</span>
            <span v-else-if="item.statut === false" class="statut refuse">Refusé</span>
            <span v-else class="statut attente">En attente de validation</span>
          </td>
          <td>{{ item.totalPoints || 0 }} points attribués</td>
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
const historique = ref([])

// Récupération des données de session
const selectionEtudiant = ref(sessionStorage.getItem('selectedEtudiant') || '')
const selectionEtudiantNom = ref(sessionStorage.getItem('selectedEtudiantNom') || '')

// Endpoints API
const PARTICIPES_SEARCH_URL = 'http://localhost:8989/api/participes/search/findByIdIdEtudiant'
const ACTIONS_URL = 'http://localhost:8989/api/actions'
const SEMESTRES_URL = 'http://localhost:8989/api/semestres'

// Charger automatiquement l'historique de l’étudiant sélectionné
function getHistorique() {
  if (!selectionEtudiant.value) return

  const url = `${PARTICIPES_SEARCH_URL}?idEtudiant=${selectionEtudiant.value}`
  fetch(url)
    .then(res => res.json())
    .then(data => {
      const participations = (data._embedded && data._embedded.participes) ? data._embedded.participes : []

      // Charger les détails de chaque participation (action et semestre)
      const fetchDetails = participations.map(item => {
        const actionUrl = `${ACTIONS_URL}/${item.id.idAction}`
        const semestreUrl = `${SEMESTRES_URL}/${item.id.idSemestre}`

        return fetch(actionUrl)
          .then(response => response.json())
          .then(actionData => {
            item.action = actionData
            return fetch(semestreUrl)
          })
          .then(response => response.json())
          .then(semestreData => {
            item.semestre = semestreData
            return item
          })
          .catch(err => {
            console.error('❌ Erreur chargement des détails :', err)
            return item // Retourne la participation même si action ou semestre échoue
          })
      })

      // Une fois toutes les requêtes terminées, mise à jour de l'historique
      return Promise.all(fetchDetails)
    })
    .then(results => {
      if (results) {
        historique.value = results
      }
    })
    .catch(err => {
      console.error('❌ Erreur chargement de l\'historique :', err)
    })
}

// Fonction pour formater la date
function formatDate(dateStr) {
  if (!dateStr) return 'N/A'
  const d = new Date(dateStr)
  return d.toLocaleDateString('fr-FR')
}

onMounted(getHistorique) // Charge l'historique au montage
</script>

<style scoped>
.historique-container {
  max-width: 900px;
  margin: 0 auto;
}

p {
  font-weight: bold;
  margin-bottom: 15px;
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
  background-color: #4caf50;
}

.statut.refuse {
  background-color: #f44336;
}

.statut.attente {
  background-color: #ff9800;
}
</style>
