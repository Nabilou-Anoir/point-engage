<template>
  <div class="main-content">
    <h2 class="title">Points cumulés</h2>

    <!-- Zone de recherche -->
    <div class="filters">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="Rechercher..."
        class="search-input"
      />
    </div>

    <!-- Tableau des étudiants avec leur total cumulatif -->
    <div class="table-container">
      <table class="custom-table">
        <thead>
        <tr>
          <th @click="sortTable('name')">Nom</th>
          <th @click="sortTable('promotion')">Promotion</th>
          <th @click="sortTable('totalPoints')">Total</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="student in paginatedStudents" :key="student.etudiantId">
          <td>{{ student.name }}</td>
          <td>{{ student.promotion }}</td>
          <td>{{ student.totalPoints.toFixed(2) }}</td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">«</button>
      <span>{{ currentPage }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">»</button>
    </div>

    <!-- Bouton d'envoi au service scolarité -->
    <div class="send-section">
      <button class="send-btn" @click="envoyerAuServiceScolarite">
        Envoyer au service de scolarité
      </button>
    </div>

    <!-- Popup de confirmation -->
    <div v-if="showConfirmationPopup" class="popup-overlay" @click.self="closeConfirmation">
      <div class="popup-box">
        <div class="popup-icon">
          <i class="fas fa-check-circle"></i>
        </div>
        <p class="popup-message">
          La liste a été envoyée au service de scolarité.
        </p>
        <div class="popup-buttons">
          <button class="btn-cancel" @click="closeConfirmation">Fermer</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import axios from "axios";

const studentsList = ref([]);
const searchQuery = ref("");
const sortColumn = ref("name");
const sortDirection = ref(1);
const currentPage = ref(1);
const itemsPerPage = 5;
const showConfirmationPopup = ref(false);
const availablePromotions = ref([]);
const baseURL = "http://localhost:8989/api";

// Stockage des IDs d'étudiants provenant d'AttribuerPoint (la liste initiale)
// Note : La clé utilisée ici doit être la même que celle renseignée dans AttribuerPoint (ex: "allProcessedStudents")
const relevantStudentIds = ref(new Set());

const sortTable = (column) => {
  if (sortColumn.value === column) {
    sortDirection.value *= -1;
  } else {
    sortColumn.value = column;
    sortDirection.value = 1;
  }
};

const sortedStudents = computed(() => {
  return [...studentsList.value].sort((a, b) => {
    if (a[sortColumn.value] > b[sortColumn.value]) return sortDirection.value;
    if (a[sortColumn.value] < b[sortColumn.value]) return -sortDirection.value;
    return 0;
  });
});

const filteredStudents = computed(() => {
  const query = searchQuery.value.toLowerCase();
  return sortedStudents.value.filter(student =>
    student.name.toLowerCase().includes(query) ||
    student.promotion.toLowerCase().includes(query)
  );
});

const totalPages = computed(() =>
  Math.max(1, Math.ceil(filteredStudents.value.length / itemsPerPage))
);

const paginatedStudents = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredStudents.value.slice(start, start + itemsPerPage);
});

const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--;
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++;
};

/**
 * Récupère la liste des IDs stockée par AttribuerPoint
 * (par exemple dans la clé "allProcessedStudents").
 */
const fetchRelevantStudentIds = () => {
  const storedIds = sessionStorage.getItem("allProcessedStudents");
  if (storedIds) {
    try {
      const parsedIds = JSON.parse(storedIds);
      relevantStudentIds.value = new Set(parsedIds);
      console.log("IDs pertinents récupérés :", parsedIds);
    } catch (e) {
      console.error("Erreur lors du parsing des IDs stockés:", e);
      relevantStudentIds.value = new Set();
    }
  } else {
    console.warn("Aucun ID stocké sous 'allProcessedStudents'.");
    relevantStudentIds.value = new Set();
  }
};

/**
 * Récupère toutes les participations et la liste complète des étudiants,
 * puis cumule les points d’action pour chaque étudiant.
 * Seuls les étudiants dont l'ID figure dans relevantStudentIds (provenant d'AttribuerPoint)
 * seront affichés, et leur total sera limité à 0,50.
 */
const fetchDataFromAPI = async () => {
  try {
    // Récupérer la liste des IDs pertinents
    fetchRelevantStudentIds();

    // Si aucun ID n'est stocké, considérer tous les étudiants comme pertinents
    const includeAllStudents = relevantStudentIds.value.size === 0;

    // Récupérer toutes les participations
    const { data: participations } = await axios.get(`${baseURL}/participes`);

    // Récupérer toutes les informations des étudiants
    const { data: etudiants } = await axios.get(`${baseURL}/etudiants`);

    // Créer un Map pour regrouper les étudiants par leur ID
    const studentMap = new Map();
    for (const etudiant of etudiants) {
      studentMap.set(etudiant.idEtudiant, {
        etudiantId: etudiant.idEtudiant,
        name: `${etudiant.prenom} ${etudiant.nom}`,
        promotion: etudiant.promotion || "Non spécifiée",
        totalPoints: 0,
        participations: []
      });
    }

    // Pour chaque participation, cumuler les points si l'étudiant est pertinent
    for (const p of participations) {
      const studentId = p.id.idEtudiant;
      if (includeAllStudents || relevantStudentIds.value.has(studentId)) {
        const student = studentMap.get(studentId);
        if (student && p.pointAction !== null) {
          let points = parseFloat(p.pointAction || 0);
          // Cumul sans dépasser 0,50
          student.totalPoints = Math.min(student.totalPoints + points, 0.50);
          student.participations.push({
            actionId: p.id.idAction,
            pointsAccordes: points
          });
        }
      }
    }

    // Conserver uniquement les étudiants avec des points > 0 ou ceux explicitement pertinents
    studentsList.value = Array.from(studentMap.values()).filter(student =>
      student.totalPoints > 0 || (includeAllStudents || relevantStudentIds.value.has(student.etudiantId))
    );

    availablePromotions.value = [...new Set(studentsList.value.map(s => s.promotion))];
  } catch (error) {
    console.error("Erreur de chargement des données:", error);
    studentsList.value = [];
  }
};

const envoyerAuServiceScolarite = () => {
  const exportList = filteredStudents.value.map(eleve => ({
    name: eleve.name,
    promotion: eleve.promotion,
    points: eleve.totalPoints.toFixed(2)
  }));
  sessionStorage.setItem("elevesFiltres", JSON.stringify(exportList));
  sessionStorage.setItem("isSent", "true");
  showConfirmationPopup.value = true;
};

const closeConfirmation = () => {
  showConfirmationPopup.value = false;
};

onMounted(() => {
  fetchDataFromAPI();
});

watch(() => sessionStorage.getItem("dataUpdated"), () => {
  if (sessionStorage.getItem("dataUpdated") === "true") {
    fetchDataFromAPI();
    sessionStorage.removeItem("dataUpdated");
  }
});
</script>

<style scoped src="./PointCommuleView.css"></style>
