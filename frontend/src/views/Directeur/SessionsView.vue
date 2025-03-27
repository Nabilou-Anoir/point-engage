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

import { Sessions } from '../../constante/SessionsViewConstants';

const sessions = ref([...Sessions]); // ✅ Copie réactive des données
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


<style scoped src="./SessionsView.css"></style>
