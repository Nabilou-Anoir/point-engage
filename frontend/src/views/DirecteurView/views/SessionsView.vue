<template>
  <div class="sessions-container">
    <!-- 🔍 Barre de recherche -->
    <div class="search-bar">
      <input type="text" placeholder="🔍 Rechercher une session..." class="search-input" />
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
          <tr v-for="session in sessions" :key="session.id">
            <td>{{ session.nom }}</td>
            <td>{{ formatDate(session.dateOuverture) }}</td>
            <td>{{ formatDate(session.dateCloture) }}</td>
            <td class="actions">
              <span class="icon edit" @click="openModal('edit', session)">✏️</span>
              <span class="icon delete" @click="deleteSession(session.id)">🗑️</span>
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
        <input type="text" v-model="currentSession.nom" placeholder="Entrez un nom" />

        <label>Date d'ouverture</label>
        <Datepicker v-model="currentSession.dateOuverture" autoApply format="dd-MM-yyyy" />

        <label>Date de clôture</label>
        <Datepicker v-model="currentSession.dateCloture" autoApply format="dd-MM-yyyy" />

        <div class="modal-actions">
          <button class="btn-confirm" @click="saveSession">✅ Confirmer</button>
          <button class="btn-cancel" @click="closeModal">❌ Annuler</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";

const sessions = ref([
  { id: 1, nom: "Semestre 1 - 2025", dateOuverture: "2021-12-08", dateCloture: "2021-12-08" },
  { id: 2, nom: "Semestre 2 - 2024", dateOuverture: "2021-12-08", dateCloture: "2021-12-08" },
  { id: 3, nom: "Semestre 1 - 2024", dateOuverture: "2021-12-08", dateCloture: "2021-12-08" },
]);

const showModal = ref(false);
const modalType = ref("add");
const currentSession = ref({ nom: "", dateOuverture: "", dateCloture: "" });

const openModal = (type, session = null) => {
  modalType.value = type;
  if (type === "edit" && session) {
    currentSession.value = { ...session };
  } else {
    currentSession.value = { nom: "", dateOuverture: "", dateCloture: "" };
  }
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

const saveSession = () => {
  if (modalType.value === "add") {
    sessions.value.push({ id: Date.now(), ...currentSession.value });
  } else {
    const index = sessions.value.findIndex((s) => s.id === currentSession.value.id);
    if (index !== -1) {
      sessions.value[index] = { ...currentSession.value };
    }
  }
  closeModal();
};

const deleteSession = (id) => {
  sessions.value = sessions.value.filter((s) => s.id !== id);
};

const formatDate = (date) => {
  if (!date) return "";
  const d = new Date(date);
  return d.toLocaleDateString("fr-FR", { day: "2-digit", month: "short", year: "numeric" });
};
</script>

<style scoped>
/* ✅ Conteneur principal */
.sessions-container {
  padding: 20px;
    font-family: 'Inter', sans-serif;
    max-width: 1300px;
    margin: 0 auto;
    background-color: #f8f9fa;
}

/* ✅ Barre de recherche */
.search-bar {
  display: flex;
  justify-content: center;
  margin-bottom: 15px;
}

.search-input {
  width: 50%;
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #ddd;
  font-size: 16px;
  transition: 0.3s;
}

.search-input:focus {
  border-color: #6a3fa0;
  box-shadow: 0 0 5px rgba(106, 63, 160, 0.5);
}

/* ✅ Header */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.btn-add {
  background: #ED6962;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 5px;
  font-size: 14px;
  cursor: pointer;
  transition: 0.3s;
}

.btn-add:hover {
  background: #6A3FA0;
}

/* ✅ Scroll pour le tableau */
.table-wrapper {
  max-height: 350px;
  overflow-y: auto;
  border-radius: 10px;
}

/* ✅ Tableau des sessions */
.sessions-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 10px;
  overflow: hidden;
}

.sessions-table th, .sessions-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.sessions-table th {
  background: #f8f8f8;
  font-weight: bold;
}

.sessions-table td {
  background: white;
}

.actions {
  text-align: center;
}

/* ✅ Icônes */
.icon {
  cursor: pointer;
  margin: 0 5px;
  font-size: 18px;
  transition: 0.3s;
}

.edit:hover {
  color: #6a3fa0;
}

.delete:hover {
  color: #d7443a;
}

/* ✅ Modale */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  animation: fadeIn 0.3s;
}

.modal-content {
  background: white;
  padding: 25px;
  border-radius: 12px;
  width: 350px;
  text-align: center;
}

.modal-content label {
  display: block;
  margin: 10px 0 5px;
  font-weight: bold;
}

.modal-content input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
}

.btn-confirm {
  background: #6A3FA0;
  color: white;
  padding: 10px;
  border-radius: 5px;
  flex: 1;
  margin-right: 10px;
}

.btn-cancel {
  background: #ED6962;
  color: white;
  padding: 10px;
  border-radius: 5px;
  flex: 1;
}

/* ✅ Animation */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}
</style>
