<template>
  <div class="main-content">
    <h2 class="title">Points cumulés</h2>

    <!-- Filtres -->
    <div class="filters">
      <select v-model="selectedSemester" class="filter-select">
        <option>Semestre 1</option>
        <option>Semestre 2</option>
      </select>

      <select v-model="selectedYear" class="filter-select">
        <option>2024/2025</option>
        <option>2023/2024</option>
        <option>2022/2023</option>
      </select>

      <input type="text" v-model="searchQuery" placeholder="Rechercher..." class="search-input" />
    </div>

    <!-- Tableau -->
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

    <!-- Pagination -->
    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">«</button>
      <span>{{ currentPage }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">»</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { students } from '../../constante/PointCommuleViewConstants';

// 📌 Définir les données localement
const studentsData = ref([...students]); // Copier les données pour éviter des problèmes de mutation

// 📌 Filtres
const selectedSemester = ref("Semestre 1");
const selectedYear = ref("2024/2025");
const searchQuery = ref("");

// 📌 Tri des colonnes
const sortColumn = ref("name");
const sortDirection = ref(1);

const sortTable = (column) => {
  if (sortColumn.value === column) {
    sortDirection.value *= -1;
  } else {
    sortColumn.value = column;
    sortDirection.value = 1;
  }

  studentsData.value = [...studentsData.value].sort((a, b) =>
    a[column] > b[column] ? sortDirection.value : -sortDirection.value
  );
};

// 📌 Filtrage des résultats
const filteredStudents = computed(() => {
  return studentsData.value.filter((student) =>
    student.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

// 📌 Pagination
const currentPage = ref(1);
const itemsPerPage = 5;

const totalPages = computed(() => Math.ceil(filteredStudents.value.length / itemsPerPage));

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
</script>

<style scoped src="./PointCommuleView.css"></style>
