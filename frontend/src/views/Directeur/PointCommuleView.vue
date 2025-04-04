<template>
  <div class="main-content">
    <h2 class="title">Points cumulés</h2>

    <div class="filters">
      <select v-model="selectedSemester" class="filter-select">
        <option>Semestre 1</option>
        <option>Semestre 2</option>
      </select>
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
          <th @click="sortTable('points')">Total</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="student in paginatedStudents" :key="student.id">
          <td>{{ student.name }}</td>
          <td>{{ student.promotion }}</td>
          <td>{{ student.points }}</td>
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
import { ref, computed, onMounted } from "vue";
import axios from "axios";

const studentsData = ref([]);
const selectedSemester = ref("Semestre 1");
const searchQuery = ref("");
const sortColumn = ref("name");
const sortDirection = ref(1);
const currentPage = ref(1);
const itemsPerPage = 5;
const showConfirmationPopup = ref(false);

const sortTable = (column) => {
  if (sortColumn.value === column) {
    sortDirection.value *= -1;
  } else {
    sortColumn.value = column;
    sortDirection.value = 1;
  }
  studentsData.value = [...studentsData.value].sort((a, b) => {
    if (a[column] > b[column]) return sortDirection.value;
    if (a[column] < b[column]) return -sortDirection.value;
    return 0;
  });
};

const filteredStudents = computed(() => {
  return studentsData.value.filter((student) => {
    const query = searchQuery.value.toLowerCase();
    return (
      (student.name.toLowerCase().includes(query) ||
        student.promotion.toLowerCase().includes(query)) &&
      student.points > 0
    );
  });
});

const totalPages = computed(() =>
  Math.ceil(filteredStudents.value.length / itemsPerPage)
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

const fetchStudents = async () => {
  try {
    const response = await axios.get("/api/etudiants/allPoints");
    studentsData.value = response.data.map((student) => ({
      id: student.id,
      name: `${student.nom} ${student.prenom}`,
      promotion: student.promotion,
      points: student.points,
    }));
  } catch (error) {
    console.error("Erreur lors de la récupération des étudiants :", error);
  }
};

onMounted(() => {
  fetchStudents();
});

const envoyerAuServiceScolarite = () => {
  sessionStorage.setItem("elevesFiltres", JSON.stringify(filteredStudents.value));
  sessionStorage.setItem("isSent", "true");
  showConfirmationPopup.value = true;
};

const closeConfirmation = () => {
  showConfirmationPopup.value = false;
};
</script>

<style scoped src="./PointCommuleView.css"></style>
