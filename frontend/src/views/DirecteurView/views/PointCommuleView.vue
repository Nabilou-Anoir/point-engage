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

// 📌 Données des étudiants
const students = ref([
  { id: 1, name: "Ines Gribaa", promotion: "FIE4", points: 0.35 },
  { id: 2, name: "Wilma Bougria", promotion: "FIE4", points: 0.5 },
  { id: 3, name: "Nabilou Anoir", promotion: "FIE4", points: 0.2 },
  { id: 4, name: "Élève 4", promotion: "FIE4", points: 0.25 },
  { id: 5, name: "Élève 5", promotion: "FIE4", points: 0.1 },
  { id: 6, name: "Élève 6", promotion: "FIE4", points: 0.4 },
]);

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
  students.value.sort((a, b) => (a[column] > b[column] ? sortDirection.value : -sortDirection.value));
};

// 📌 Filtrage des résultats
const filteredStudents = computed(() => {
  return students.value.filter((student) =>
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

<style scoped>
.main-content {
  padding: 20px;
  font-family: 'Inter', sans-serif;
  max-width: 1300px;
  margin: 0 auto;
  background-color: #f8f9fa;
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #4a1f80;
  margin-bottom: 25px;
  text-align: start;
}

/* 📌 Filtres */
.filters {
  display: flex;
  gap: 10px;
  margin-bottom: 25px;
  align-items: center;
}

.filter-select {
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #ddd;
  background-color: white;
  font-size: 14px;
  color: #333;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.filter-select:focus {
  border-color: #6A3FA0;
  box-shadow: 0 0 8px rgba(106, 63, 160, 0.2);
}

.search-input {
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #ddd;
  background-color: white;
  font-size: 14px;
  color: #333;
  flex-grow: 1;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.search-input:focus {
  border-color: #6A3FA0;
  box-shadow: 0 0 8px rgba(106, 63, 160, 0.2);
}



/* 📌 Tableau */
.table-container {
  background: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.custom-table {
  width: 100%;
  border-collapse: collapse;
}

.custom-table th {
  
  padding: 14px;
  cursor: pointer;
  font-size: 14px;
  color: #6A3FA0;
  border-bottom: 2px solid #ddd;
  text-align: left;
}

.custom-table th:hover {
  background: linear-gradient(135deg, #8E5ACD, #6A3FA0);
}

.custom-table td {
  padding: 14px;
  border-bottom: 1px solid #ddd;
  font-size: 14px;
  color: #555;
}

.custom-table tr:hover {
  background-color: rgba(106, 63, 160, 0.05);
}

/* 📌 Pagination */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 25px;
}

.pagination button {
  background: linear-gradient(135deg, #6A3FA0, #8E5ACD);
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.3s ease, transform 0.2s ease;
  margin: 0 5px;
}

.pagination button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.pagination button:hover:not(:disabled) {
  background: linear-gradient(135deg, #8E5ACD, #6A3FA0);
  transform: translateY(-2px);
}

.pagination button:active:not(:disabled) {
  transform: translateY(0);
}

.pagination span {
  margin: 0 15px;
  font-size: 14px;
  color: #4a1f80;
  font-weight: 500;
}
</style>