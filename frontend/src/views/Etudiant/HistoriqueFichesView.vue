<template>
  <div class="activities-container">
    <h1>Historique des fiches</h1>

    <p v-if="etudiant">
      <strong>Étudiant connecté :</strong> {{ etudiant.nom }} {{ etudiant.prenom }}
    </p>

    <div class="search-filter">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="Rechercher par nom d'action"
        class="search-input"
      />
    </div>

    <div class="table-container" v-if="paginatedHistorique.length > 0">
      <table class="activities-table">
        <thead>
        <tr>
          <th>Action</th>
          <th>Date de Réalisation</th>
          <th class="text-right">Statut</th>
          <th class="text-right">Total Points</th>
          <th class="text-right">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr
          v-for="(item, index) in paginatedHistorique"
          :key="`${item.id.idEtudiant}-${item.id.idAction}-${item.id.idSemestre}`"
          class="table-row"
        >
          <!-- En cliquant sur le nom de l'action, on ouvre le popup de détails -->
          <td @click="showActionPopup(item)" class="activity-name">
            {{ item.action?.nom || 'N/A' }}
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
          <td class="text-right">
            <!-- Bouton "Suivre" conserve son libellé et ouvre le popup de suivi -->
            <button class="btn-suivre" @click.stop="showSuiviPopup(item)">Suivre</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div v-else>
      <p>Aucun historique disponible.</p>
    </div>

    <div class="pagination" v-if="totalPages > 1">
      <button @click="prevPage" :disabled="currentPage === 1" class="pagination-button">
        Précédent
      </button>
      <span class="page-indicator">Page {{ currentPage }} sur {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages" class="pagination-button">
        Suivant
      </button>
    </div>

    <!-- Popup pour afficher les détails de l'action -->
    <div v-if="actionPopupVisible" class="modal-overlay">
      <div class="modal-content">
        <h2>Détails de l'Action</h2>
        <p>
          <strong>Nom de l'action :</strong>
          {{ selectedAction?.action?.nom || 'N/A' }}
        </p>
        <p>
          <strong>Description de la participation :</strong>
          {{ selectedAction?.descriptionParticipation || 'Aucune description renseignée' }}
        </p>
        <p>
          <strong>Date de saisie :</strong>
          {{ formatDate(selectedAction?.dateDebutParticipation) }}
        </p>
        <button class="modal-close-button" @click="actionPopupVisible = false">
          Fermer
        </button>
      </div>
    </div>

    <!-- Popup pour le suivi de la fiche -->
    <div v-if="suiviVisible" class="modal-overlay">
      <div class="modal-content">
        <h2>Suivi de la Participation</h2>
        <!-- Ici, affichez l'évolution du suivi de la fiche (soumission, validation, etc.) -->
        <p>
          <strong>Action :</strong> {{ selectedSuivi?.action?.nom || 'N/A' }}
        </p>
        <p>
          <strong>Date de Réalisation :</strong> {{ formatDate(selectedSuivi?.dateDebutParticipation) }}
        </p>
        <!-- Ajoutez d'autres éléments d'évolution de suivi ici selon vos besoins -->
        <button class="modal-close-button" @click="suiviVisible = false">
          Fermer
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';

const historique = ref([]);
const etudiant = ref(null);
const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = ref(5);

// États pour les deux popups
const selectedAction = ref(null);
const actionPopupVisible = ref(false);
const selectedSuivi = ref(null);
const suiviVisible = ref(false);

const loggedInUser = JSON.parse(sessionStorage.getItem('loggedInUser'));

async function getHistorique() {
  if (!loggedInUser?.email) return;
  // Récupération de l'étudiant par email
  const etuRes = await fetch(`http://localhost:8989/api/etudiants/byEmail?email=${loggedInUser.email}`);
  const etudiantData = await etuRes.json();
  etudiant.value = etudiantData;
  // Récupération des participations associées à l'étudiant
  const partRes = await fetch(`http://localhost:8989/api/participes/search/findByIdIdEtudiant?idEtudiant=${etudiantData.idEtudiant}`);
  const data = await partRes.json();
  const participations = data._embedded?.participes || [];
  // Pour chaque participation, récupérer l'action et le semestre
  for (const item of participations) {
    const actionRes = await fetch(`http://localhost:8989/api/actions/${item.id.idAction}`);
    const semRes = await fetch(`http://localhost:8989/api/semestres/${item.id.idSemestre}`);
    item.action = await actionRes.json();
    item.semestre = await semRes.json();
  }
  historique.value = participations;
}

function formatDate(dateStr) {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  return d.toLocaleDateString('fr-FR');
}

const filteredHistorique = computed(() => {
  return !searchQuery.value
    ? historique.value
    : historique.value.filter((item) =>
      item.action?.nom?.toLowerCase().includes(searchQuery.value.toLowerCase())
    );
});

const totalPages = computed(() => Math.ceil(filteredHistorique.value.length / itemsPerPage.value));
const paginatedHistorique = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  return filteredHistorique.value.slice(start, start + itemsPerPage.value);
});

function nextPage() {
  if (currentPage.value < totalPages.value) currentPage.value++;
}
function prevPage() {
  if (currentPage.value > 1) currentPage.value--;
}

// Ouvre le popup de détails de l'action
function showActionPopup(item) {
  selectedAction.value = item;
  actionPopupVisible.value = true;
}

// Ouvre le popup de suivi
function showSuiviPopup(item) {
  selectedSuivi.value = item;
  suiviVisible.value = true;
}

onMounted(getHistorique);
</script>

<style scoped>
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

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
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

/* Bouton "Suivre" */
.btn-suivre {
  background-color: #6A3FA0;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 12px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-suivre:hover {
  background-color: #5a2f8f;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(106, 63, 160, 0.3);
}
</style>
