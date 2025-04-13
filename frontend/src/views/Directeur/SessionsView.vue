<template>
  <div class="semestres-container">
    <!-- 🔍 Barre de recherche -->
    <div class="search-bar">
      <input
        type="text"
        v-model="search"
        placeholder="🔍 Rechercher une semestre..."
        class="search-input"
      />
    </div>

    <!-- 📅 Header avec titre et bouton -->
    <div class="header">
      <h2> Les semestres</h2>
      <button class="btn-add" @click="openModal('add')">➕ Ajouter une semestre</button>
    </div>

    <!-- 🗓️ Saisie des dates de dépôt globales -->
    <div class="global-dates">
      <label>Date début dépôt (global)</label>
      <Datepicker v-model="globalDebutDepot" autoApply format="yyyy-MM-dd" />

      <label>Date fin dépôt (global)</label>
      <Datepicker v-model="globalFinDepot" autoApply format="yyyy-MM-dd" />

      <button class="btn-confirm" @click="applyGlobalDates">📝 Appliquer à tous</button>
    </div>

    <!-- 📌 Tableau des semestres -->
    <div class="table-wrapper">
      <table class="semestres-table">
        <thead>
        <tr>
          <th>Nom de la semestre</th>
          <th>Date d'ouverture</th>
          <th>Date de clôture</th>
          <th>Date début dépôt</th>
          <th>Date fin dépôt</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="semestre in filteredSemestres" :key="semestre.idSemestre">
          <td>Semestre {{ semestre.nbSemestre }}</td>
          <td>{{ formatDate(semestre.dateDebutSemestre) }}</td>
          <td>{{ formatDate(semestre.dateFinSemestre) }}</td>
          <td>{{ formatDate(semestre.dateDebutDepot) }}</td>
          <td>{{ formatDate(semestre.dateFinDepot) }}</td>
          <td class="actions">
            <span class="icon edit" @click="openModal('edit', semestre)">✏️</span>
            <span class="icon delete" @click="deleteSemestre(semestre.idSemestre)">🗑️</span>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- 🛠️ MODALE pour ajouter/éditer une semestre -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h3>{{ modalType === 'add' ? '➕ Ajouter une semestre' : '✏️ Modifier la semestre' }}</h3>

        <label>Nom de la semestre</label>
        <div class="custom-select-wrapper">
          <select v-model="currentSemestre.nbSemestre" class="custom-select">
            <option disabled value="">-- Choisir une semestre --</option>
            <option :value="1">Semestre 1</option>
            <option :value="2">Semestre 2</option>
            <option :value="3">Semestre 3</option>
            <option :value="4">Semestre 4</option>
            <option :value="5">Semestre 5</option>
            <option :value="6">Semestre 6</option>
            <option :value="7">Semestre 7</option>
            <option :value="8">Semestre 8</option>
            <option :value="9">Semestre 9</option>
            <option :value="10">Semestre 10</option>
          </select>
        </div>

        <label>Date d'ouverture</label>
        <Datepicker v-model="currentSemestre.dateDebutSemestre" autoApply format="yyyy-MM-dd" />

        <label>Date de clôture</label>
        <Datepicker v-model="currentSemestre.dateFinSemestre" autoApply format="yyyy-MM-dd" />

        <div class="modal-actions">
          <button class="btn-confirm" @click="saveSemestre">✅ Confirmer</button>
          <button class="btn-cancel" @click="closeModal">❌ Annuler</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import axios from "axios";
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";

const API_URL = "/api/semestres";

const semestres = ref([]);
const showModal = ref(false);
const modalType = ref("add");
const search = ref("");
const globalDebutDepot = ref(null);
const globalFinDepot = ref(null);
const currentSemestre = ref({
  idSemestre: null,
  nbSemestre: "",
  dateDebutSemestre: null,
  dateFinSemestre: null,
});

const fetchSemestres = async () => {
  try {
    const res = await axios.get(API_URL);
    semestres.value = res.data._embedded?.semestres || res.data;
  } catch (err) {
    console.error("Erreur de chargement des semestres :", err);
  }
};

onMounted(fetchSemestres);

const openModal = (type, semestre = null) => {
  modalType.value = type;
  if (type === "edit" && semestre) {
    currentSemestre.value = { ...semestre };
  } else {
    currentSemestre.value = {
      idSemestre: null,
      nbSemestre: "",
      dateDebutSemestre: null,
      dateFinSemestre: null,
    };
  }
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

const saveSemestre = async () => {
  const s = currentSemestre.value;
  if (!s.nbSemestre || !s.dateDebutSemestre || !s.dateFinSemestre) {
    alert("🚨 Veuillez remplir tous les champs obligatoires !");
    return;
  }
  try {
    if (modalType.value === "add") {
      const res = await axios.post(API_URL, s);
      semestres.value.push(res.data);
    } else {
      await axios.put(`${API_URL}/${s.idSemestre}`, s);
      const index = semestres.value.findIndex(sem => sem.idSemestre === s.idSemestre);
      if (index !== -1) semestres.value[index] = { ...s };
    }
    closeModal();
  } catch (err) {
    console.error("Erreur lors de la sauvegarde :", err);
  }
};

const applyGlobalDates = async () => {
  if (!globalDebutDepot.value || !globalFinDepot.value) {
    alert("Veuillez renseigner les deux dates de dépôt.");
    return;
  }
  try {
    for (const semestre of semestres.value) {
      semestre.dateDebutDepot = globalDebutDepot.value;
      semestre.dateFinDepot = globalFinDepot.value;
      await axios.put(`${API_URL}/${semestre.idSemestre}`, semestre);
    }
  } catch (err) {
    console.error("Erreur lors de l'application globale des dates :", err);
  }
};

const deleteSemestre = async (id) => {
  try {
    await axios.delete(`${API_URL}/${id}`);
    semestres.value = semestres.value.filter(s => s.idSemestre !== id);
  } catch (err) {
    console.error("Erreur lors de la suppression :", err);
  }
};

const formatDate = (date) => {
  if (!date) return "";
  const d = new Date(date);
  return d.toLocaleDateString("fr-FR", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });
};

const filteredSemestres = computed(() => {
  if (!search.value) return semestres.value;
  const query = search.value.toLowerCase();
  return semestres.value.filter((s) =>
    (`${s.nbSemestre}`.includes(query) ||
      formatDate(s.dateDebutSemestre).toLowerCase().includes(query) ||
      formatDate(s.dateFinSemestre).toLowerCase().includes(query) ||
      formatDate(s.dateDebutDepot).toLowerCase().includes(query) ||
      formatDate(s.dateFinDepot).toLowerCase().includes(query))
  );
});
</script>

<style scoped src="./SessionsView.css"></style>
