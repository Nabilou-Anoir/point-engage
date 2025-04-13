<template>
  <div class="main-layout">
    <!-- Sidebar : Affichage des groupes FIE/FIA et boutons pour chaque semestre -->
    <div class="sidebar">
      <h3>Historique</h3>
      <div class="filter-options">
        <div
          v-for="(group, groupIndex) in groupedSemestres"
          :key="group.groupName"
          class="annee-group"
        >
          <h4 @click="toggleGroup(groupIndex)" :class="{ expanded: group.showSemestres }">
            {{ group.groupName }}
          </h4>
          <div class="semestre-group" v-if="group.showSemestres">
            <button
              v-for="sem in group.semestres"
              :key="sem.idSemestre"
              class="semestre-btn"
              @click="setSemestreFilter(sem)"
              :class="{ active: selectedSemestre === sem.nbSemestre }"
            >
              Semestre {{ sem.nbSemestre }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Contenu principal : affichage de l'historique des participations -->
    <div class="main-content">
      <!-- Filtre global -->
      <div class="top-filter">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Rechercher par nom, promotion ou action..."
          class="search-input"
          @input="applyLocalFilter"
        />
        <button class="clear-filter-btn" @click="clearLocalFilters">
          Afficher tous
        </button>
      </div>

      <h1 class="title">Historique des participations</h1>

      <!-- Tableau des participations filtrées -->
      <div class="table-container" v-if="paginatedParticipations.length > 0">
        <table class="styled-table">
          <thead>
          <tr>
            <th>Nom Prenom</th>
            <th>Promotion</th>
            <th>Action</th>
            <th>Résumé du contenu</th>
            <th>Validité</th>
            <th>Nombre points</th>
            <th>Remarque</th>
            <th>Info</th>
          </tr>
          </thead>
          <tbody>
          <tr
            v-for="part in paginatedParticipations"
            :key="`${part.id.idEtudiant}-${part.id.idAction}-${part.id.idSemestre}`"
          >
            <td>{{ part.etudiant?.nom || '-' }} {{ part.etudiant?.prenom || '' }}</td>
            <td>{{ part.etudiant?.promotion || '-' }}</td>
            <td>{{ part.action?.referentiel?.nom || part.action?.nom || '-' }}</td>
            <td>{{ part.resumeDirecteur || '-' }}</td>
            <td class="text-right">
              <span v-if="part.statut === true" class="status-valid">Validé</span>
              <span v-else-if="part.statut === false" class="status-pending">Refusé</span>
              <span v-else class="status-default">En attente</span>
            </td>
            <td class="text-right">
              <span class="points">{{ part.pointAction || 0 }} pts</span>
            </td>
            <td>{{ part.remarqueReferent || '-' }}</td>
            <td class="text-right">
              <span class="info-emoji" @click.stop="showSuiviPopup(part)">ℹ️</span>
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
    </div>

    <!-- Popup pour le suivi de la participation -->
    <div v-if="suiviVisible" class="modal-overlay">
      <div class="modal-content">
        <h2>Suivi de la Participation</h2>
        <p>
          <strong>Nom de l'étudiant :</strong>
          {{ selectedSuivi?.etudiant?.nom || '-' }} {{ selectedSuivi?.etudiant?.prenom || '' }}
        </p>
        <p>
          <strong>Information :</strong>
          {{ selectedSuivi?.action?.nom || '-' }}
        </p>
        <p>
          <strong>Date de saisie :</strong>
          {{ formatDate(selectedSuivi?.dateDebutParticipation) }}
        </p>
        <p>
          <strong>Année universitaire :</strong>
          {{ formatAnneeScolaire(selectedSuivi?.semestre?.anneeUniversitaire) }}
        </p>
        <p>
          <strong>Semestre :</strong>
          {{ selectedSuivi?.semestre?.nbSemestre || '-' }}
        </p>
        <p>
          <strong>Statut :</strong>
          <span v-if="selectedSuivi?.statut === true" class="status-valid">Validé</span>
          <span v-else-if="selectedSuivi?.statut === false" class="status-pending">Refusé</span>
          <span v-else class="status-default">En attente</span>
        </p>
        <p>
          <strong>Points :</strong>
          {{ selectedSuivi?.pointAction || 0 }} pts
        </p>
        <p>
          <strong>Résumé :</strong>
          {{ selectedSuivi?.resumeDirecteur || '-' }}
        </p>
        <p>
          <strong>Remarque du référent :</strong>
          {{ selectedSuivi?.remarqueReferent || '-' }}
        </p>
        <p>
          <strong>Description Participation :</strong>
          {{ selectedSuivi?.descriptionParticipation || '-' }}
        </p>
        <button class="modal-close-button" @click="suiviVisible = false">
          Fermer
        </button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, computed, onMounted } from "vue";

const allHistorique = ref([]);
const historique = ref([]);
const searchQuery = ref("");
const selectedSemestre = ref(null);
const selectedYearStart = ref(null);
const selectedYearEnd = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(10);
const selectedSuivi = ref(null);
const suiviVisible = ref(false);
const semestres = ref([]);
const groupedSemestres = ref([]);

function computeGroupedSemestres() {
  const groups = {
    "FIE 1": [],
    "FIE 2": [],
    "FIE 3/FIA 3": [],
    "FIE 4/FIA 4": [],
    "FIE 5/FIA 5": []
  };

  semestres.value.forEach(sem => {
    const nb = parseInt(sem.nbSemestre, 10);
    if (nb === 1 || nb === 2) groups["FIE 1"].push(sem);
    else if (nb === 3 || nb === 4) groups["FIE 2"].push(sem);
    else if (nb === 5 || nb === 6) groups["FIE 3/FIA 3"].push(sem);
    else if (nb === 7 || nb === 8) groups["FIE 4/FIA 4"].push(sem);
    else if (nb === 9 || nb === 10) groups["FIE 5/FIA 5"].push(sem);
  });

  groupedSemestres.value = Object.keys(groups).map(key => ({
    groupName: key,
    semestres: groups[key],
    showSemestres: true
  }));
}

async function getSemestres() {
  try {
    const res = await fetch("http://localhost:8989/api/semestres");
    semestres.value = await res.json();
    computeGroupedSemestres();
  } catch (error) {
    console.error("Erreur récupération semestres:", error);
  }
}

async function enrichParticipation(part) {
  const [etudiantRes, actionRes, semestreRes] = await Promise.all([
    fetch(`http://localhost:8989/api/etudiants/${part.id.idEtudiant}`),
    fetch(`http://localhost:8989/api/actions/${part.id.idAction}`),
    fetch(`http://localhost:8989/api/semestres/${part.id.idSemestre}`)
  ]);

  const [etudiant, action, semData] = await Promise.all([
    etudiantRes.json(),
    actionRes.json(),
    semestreRes.json()
  ]);

  part.etudiant = etudiant;
  part.action = action;
  part.semestre = semData;

  if (action.idReferentiel) {
    const refRes = await fetch(`http://localhost:8989/api/referentiels/${action.idReferentiel}`);
    action.referentiel = await refRes.json();
  }

  // Remplacer totalPoints par pointsAction si disponible
  part.pointAction= part.pointAction || 0;
}

async function getAllParticipations() {
  try {
    const res = await fetch("http://localhost:8989/api/participes");
    const data = await res.json();
    const enriched = [];
    for (const part of data) {
      try {
        await enrichParticipation(part);
        enriched.push(part);
      } catch (error) {
        console.error("Erreur enrichissement:", error);
      }
    }
    allHistorique.value = enriched;
    if (!selectedSemestre.value) historique.value = enriched;
  } catch (error) {
    console.error("Erreur récupération participations:", error);
  }
}

async function setSemestreFilter(sem) {
  selectedSemestre.value = sem.nbSemestre;
  const d = new Date(sem.anneeUniversitaire);
  const year = d.getFullYear();
  selectedYearStart.value = year;
  selectedYearEnd.value = year + 1;

  try {
    const res = await fetch(
      `http://localhost:8989/api/participes/byYearAndSemestre?year=${year}&semestre=${sem.nbSemestre}`
    );
    const data = await res.json();
    const enriched = [];
    for (const part of data) {
      try {
        await enrichParticipation(part);
        enriched.push(part);
      } catch (error) {
        console.error("Erreur enrichissement filtre:", error);
      }
    }
    historique.value = enriched;
    currentPage.value = 1;
  } catch (error) {
    console.error("Erreur fetch dans setSemestreFilter:", error);
  }
}

function applyLocalFilter() {
  currentPage.value = 1;
}

const filteredParticipations = computed(() => {
  const baseList = searchQuery.value.trim() ? allHistorique.value : historique.value;
  const q = searchQuery.value.toLowerCase();
  return baseList.filter((part) => {
    const nom = (part.etudiant?.nom || "").toLowerCase();
    const promo = (part.etudiant?.promotion || "").toLowerCase();
    const actionName = (part.action?.referentiel?.nom || part.action?.nom || "").toLowerCase();
    return nom.includes(q) || promo.includes(q) || actionName.includes(q);
  });
});

const totalPages = computed(() =>
  Math.ceil(filteredParticipations.value.length / itemsPerPage.value)
);

const paginatedParticipations = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  return filteredParticipations.value.slice(start, start + itemsPerPage.value);
});

function nextPage() {
  if (currentPage.value < totalPages.value) currentPage.value++;
}

function prevPage() {
  if (currentPage.value > 1) currentPage.value--;
}

function clearLocalFilters() {
  searchQuery.value = "";
  selectedSemestre.value = null;
  selectedYearStart.value = null;
  selectedYearEnd.value = null;
  historique.value = allHistorique.value;
}

function toggleGroup(index) {
  groupedSemestres.value[index].showSemestres = !groupedSemestres.value[index].showSemestres;
}

function formatDate(dateStr) {
  if (!dateStr) return "";
  return new Date(dateStr).toLocaleDateString("fr-FR");
}

function formatAnneeScolaire(dateStr) {
  if (!dateStr) return "";
  const d = new Date(dateStr);
  const year = d.getFullYear();
  return `${year}/${year + 1}`;
}

function showSuiviPopup(item) {
  selectedSuivi.value = item;
  suiviVisible.value = true;
}

onMounted(async () => {
  await getAllParticipations();
  await getSemestres();

  setInterval(async () => {
    if (!selectedSemestre.value) {
      await getAllParticipations();
    } else {
      await setSemestreFilter({
        nbSemestre: selectedSemestre.value,
        anneeUniversitaire: `${selectedYearStart.value}-09-01`,
      });
    }
  }, 30000);
});
</script>

<style scoped>
/* 📌 Sidebar fine et moderne */
.sidebar {
  width: 240px;
  background-color: #ffffff;
  padding: 24px;
  border-right: 1px solid #eaeef3;
  position: fixed;
  height: 100vh;
  overflow-y: auto;
}

.sidebar h3 {
  font-size: 18px;
  margin-bottom: 24px;
  color: #2c3e50;
  font-weight: 600;
  letter-spacing: -0.5px;
}

.filter-options {
  display: flex;
  flex-direction: column;
}

.annee-group {
  margin-bottom: 16px;
}

.annee-group h4 {
  cursor: pointer;
  font-size: 15px;
  margin-bottom: 12px;
  color: #34495e;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: color 0.3s ease;
}

.annee-group h4:hover {
  color: #6a3fa0;
}

.annee-group h4::before {
  content: "▸";
  font-size: 12px;
  color: #6a3fa0;
  transition: transform 0.3s ease;
}

.annee-group h4.expanded::before {
  transform: rotate(90deg);
}

.semestre-group {
  margin-left: 16px;
  padding-left: 8px;
  border-left: 2px solid #eaeef3;
}

.semestre-btn {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 10px 12px;
  margin-bottom: 6px;
  background-color: transparent;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  text-align: left;
  transition: all 0.3s ease;
  color: #555;
  font-size: 14px;
}

.semestre-btn:hover {
  background-color: #f8f9fa;
  color: #6a3fa0;
  transform: translateX(4px);
}

.semestre-btn.active {
  background-color: #6a3fa0;
  color: white;
  font-weight: 500;
}

.semestre-btn::before {
  content: "•";
  margin-right: 8px;
  font-size: 16px;
  color: #6a3fa0;
  transition: color 0.3s ease;
}

.semestre-btn.active::before {
  content: "✔";
  color: white;
}

/* 📌 Main content et filtres */
.main-content {
  padding-top: 37px;
  font-family: 'Inter', sans-serif;
  max-width: 1100px;
  margin: 0 auto;
  background-color: #f8f9fa;
  padding: 37px 24px;
}

.top-filter {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  width: 70%;
  transition: border-color 0.3s ease;
}

.search-input:focus {
  border-color: #6a3fa0;
  outline: none;
}

.clear-filter-btn {
  padding: 10px 20px;
  background-color: #6a3fa0;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.clear-filter-btn:hover {
  background-color: #5a2f90;
}

/* 📌 Titre */
.title {
  font-size: 24px;
  font-weight: 600;
  color: #6a3fa0;
  margin-bottom: 20px;
  text-align: start;
}

/* 📊 Tableau */
.table-container {
  width: 100%;
  overflow-x: auto;
  background: #ffffff;
  border-radius: 12px;
  margin-bottom: 20px;
}

.styled-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: auto;
}

.styled-table th,
.styled-table td {
  padding: 14px;
  text-align: left;
  border-bottom: 1px solid #eee;
  word-wrap: break-word;
}

.styled-table th {
  background: #f8f8f8;
  font-weight: 600;
  color: #555;
}

.styled-table tbody tr:hover {
  background: #f5f5f5;
}

/* 📌 Bouton Information */
.info-emoji {
  cursor: pointer;
  font-size: 18px;
  color: #6a3fa0;
  background: none;
  border: none;
  padding: 0;
  outline: none;
  transition: color 0.3s ease;
}

.info-emoji:hover {
  color: #5a2f90;
}

/* 📌 Pagination */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  gap: 10px;
}

.pagination-button {
  padding: 10px 20px;
  background-color: #6a3fa0;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
  font-weight: 500;
}

.pagination-button:disabled {
  background-color: #cbd5e0;
  cursor: not-allowed;
}

.pagination-button:hover:not(:disabled) {
  background-color: #5a2f90;
}

.page-indicator {
  font-size: 14px;
}

/* 📌 Popup */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  max-width: 500px;
  width: 100%;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-content h2 {
  margin-top: 0;
  color: #6a3fa0;
  margin-bottom: 20px;
}

.modal-close-button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #6a3fa0;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.modal-close-button:hover {
  background-color: #5a2f90;
}

/* Responsive */
@media (max-width: 768px) {
  .top-filter {
    flex-direction: column;
  }
  .clear-filter-btn {
    margin-top: 10px;
  }
}
</style>
