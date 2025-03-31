<template>
  <div class="sessions-container">
    <!-- 🔍 Barre de recherche -->
    <div class="search-bar">
      <input
        type="text"
        v-model="search"
        placeholder="🔍 Rechercher une session..."
        class="search-input"
      />
    </div>

    <!-- 📅 Header avec titre et bouton -->
    <div class="header">
      <h2> Les sessions</h2>
      <button class="btn-add" @click="openModal('add')">➕ Ajouter une session</button>
    </div>

    <!-- 📌 Tableau des sessions -->
    <div class="table-wrapper">
      <table class="sessions-table">
        <thead>
          <tr>
            <th>Nom de la session</th>
            <th>Date d'ouverture</th>
            <th>Date de clôture</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="session in filteredSessions" :key="session.idSemestre">
            <td>Session {{ session.nbSemestre }}</td>
            <td>{{ formatDate(session.dateDebutSemestre) }}</td>
            <td>{{ formatDate(session.dateFinSemestre) }}</td>
            <td class="actions">
              <span class="icon edit" @click="openModal('edit', session)">✏️</span>
              <span class="icon delete" @click="deleteSession(session.idSemestre)">🗑️</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 🛠️ MODALE -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h3>{{ modalType === 'add' ? '➕ Ajouter une session' : '✏️ Modifier la session' }}</h3>

        <label>Nom de la session</label>
        <div class="custom-select-wrapper">
          <select v-model="currentSession.nbSemestre" class="custom-select">
            <option disabled value="">-- Choisir une session --</option>
            <option :value="1">Session 1</option>
            <option :value="2">Session 2</option>
          </select>
        </div>

        <label>Date d'ouverture</label>
        <Datepicker v-model="currentSession.dateDebutSemestre" autoApply format="yyyy-MM-dd" />

        <label>Date de clôture</label>
        <Datepicker v-model="currentSession.dateFinSemestre" autoApply format="yyyy-MM-dd" />

        <div class="modal-actions">
          <button class="btn-confirm" @click="saveSession">✅ Confirmer</button>
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

const sessions = ref([]);
const showModal = ref(false);
const modalType = ref("add");
const search = ref("");
const currentSession = ref({
  idSemestre: null,
  nbSemestre: "",
  dateDebutSemestre: null,
  dateFinSemestre: null,
});

const fetchSessions = async () => {
  try {
    const res = await axios.get(API_URL);
    if (res.data._embedded && res.data._embedded.semestres) {
      sessions.value = res.data._embedded.semestres;
    } else {
      sessions.value = res.data;
    }
  } catch (err) {
    console.error("Erreur de chargement des sessions :", err);
  }
};

onMounted(fetchSessions);

const openModal = (type, session = null) => {
  modalType.value = type;
  if (type === "edit" && session) {
    currentSession.value = { ...session };
  } else {
    currentSession.value = {
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

const saveSession = async () => {
  if (!currentSession.value.nbSemestre || !currentSession.value.dateDebutSemestre || !currentSession.value.dateFinSemestre) {
    alert("🚨 Veuillez remplir tous les champs obligatoires !");
    return;
  }

  try {
    if (modalType.value === "add") {
      const res = await axios.post(API_URL, currentSession.value);
      sessions.value.push(res.data);
    } else {
      await axios.put(`${API_URL}/${currentSession.value.idSemestre}`, currentSession.value);
      const index = sessions.value.findIndex(s => s.idSemestre === currentSession.value.idSemestre);
      if (index !== -1) sessions.value[index] = { ...currentSession.value };
    }
    closeModal();
  } catch (err) {
    console.error("Erreur lors de la sauvegarde :", err);
  }
};

const deleteSession = async (id) => {
  try {
    await axios.delete(`${API_URL}/${id}`);
    sessions.value = sessions.value.filter(s => s.idSemestre !== id);
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

const filteredSessions = computed(() => {
  if (!search.value) return sessions.value;

  const query = search.value.toLowerCase();

  return sessions.value.filter((s) =>
    (`${s.nbSemestre}`.includes(query) ||
     formatDate(s.dateDebutSemestre).toLowerCase().includes(query) ||
     formatDate(s.dateFinSemestre).toLowerCase().includes(query))
  );
});
</script>

<style scoped src="./SessionsView.css"></style>
