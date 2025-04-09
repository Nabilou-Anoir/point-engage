<template>
  <div class="main-content">
    <h2 class="title">Points cumulés</h2>

    <div class="filters">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="Rechercher..."
        class="search-input"
      />
    </div>

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

    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">«</button>
      <span>{{ currentPage }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">»</button>
    </div>

    <div class="send-section">
      <button class="send-btn" @click="envoyerAuServiceScolarite">
        Envoyer au service de scolarité
      </button>
    </div>

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
import { ref, computed, onMounted, onUnmounted, watch } from "vue";
import axios from "axios";

const studentsList = ref([]);
const selectedPromotion = ref("");
const availablePromotions = ref([]);
const searchQuery = ref("");
const sortColumn = ref("name");
const sortDirection = ref(1);
const currentPage = ref(1);
const itemsPerPage = 5;
const showConfirmationPopup = ref(false);
const baseURL = "http://localhost:8989/api";

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
  return sortedStudents.value.filter((student) => {
    const query = searchQuery.value.toLowerCase();
    const matchesPromotion = selectedPromotion.value ? student.promotion === selectedPromotion.value : true;
    const matchesSearch = student.name.toLowerCase().includes(query) ||
      student.promotion.toLowerCase().includes(query);
    return matchesPromotion && matchesSearch;
  });
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

const processStudentData = (participations) => {
  const studentMap = new Map();

  participations.forEach(p => {
    const studentId = p.etudiantId;
    if (!studentMap.has(studentId)) {
      studentMap.set(studentId, {
        etudiantId: studentId,
        name: p.name,
        promotion: p.promotion,
        totalPoints: 0,
        participations: []
      });
    }

    const student = studentMap.get(studentId);
    let points = parseFloat(p.pointsAccordes || 0);
    points = Math.min(points, 0.5);
    student.totalPoints = Math.min(student.totalPoints + points, 0.5);
    student.participations.push({
      engagementType: p.engagementType,
      actionType: p.actionType || "N/A",
      pointsAccordes: points
    });
  });

  studentsList.value = Array.from(studentMap.values());
  availablePromotions.value = [...new Set(studentsList.value.map(s => s.promotion))];
  sessionStorage.setItem("pointsCumules", JSON.stringify(studentsList.value));
};

const fetchDataFromAPI = async () => {
  try {
    const { data: participations } = await axios.get(`${baseURL}/participes`);
    const enrichedParticipations = await Promise.all(participations.map(async (p) => {
      try {
        const [etudiant, action, referentiel] = await Promise.all([
          axios.get(`${baseURL}/etudiants/${p.id.idEtudiant}`),
          axios.get(`${baseURL}/actions/${p.id.idAction}`),
          axios.get(`${baseURL}/referentiels/${p.idReferentiel || 1}`)
        ]);

        return {
          etudiantId: etudiant.data.idEtudiant,
          name: `${etudiant.data.prenom} ${etudiant.data.nom}`,
          promotion: etudiant.data.promotion,
          engagementType: referentiel.data.nom || "N/A",
          actionType: action.data.nom,
          pointsAccordes: parseFloat(p.pointAction || 0)
        };
      } catch (e) {
        console.error("Erreur sur une participation:", e);
        return null;
      }
    }));

    const validParticipations = enrichedParticipations.filter(Boolean);
    processStudentData(validParticipations);
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
  const participationsData = sessionStorage.getItem("participationsAttribuées");
  if (participationsData) {
    try {
      const participations = JSON.parse(participationsData);
      processStudentData(participations);
    } catch {
      fetchDataFromAPI();
    }
  } else {
    fetchDataFromAPI();
  }
});
</script>

<style scoped src="./PointCommuleView.css"></style>
