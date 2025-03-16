<template>
    <!-- Contenu principal -->
    <div class="main-content">
      <!-- Référents -->
      <h2 class="title">Référents</h2>
      <div class="table-container">
        <table class="custom-table">
          <thead>
            <tr>
              <th>Nom & Prénom</th>
              <th>Activité</th>
              <th>Engagement</th>
              <th>Modifier</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="referent in referents" :key="referent.id">
              <td>{{ referent.name }}</td>
              <td>{{ referent.activity }}</td>
              <td>{{ referent.engagement }}</td>
              <td>
                <button class="btn delete" @click="deleteReferent(referent.id)">🗑</button>
                <button class="btn edit" @click="openModal('editReferent', referent)">Modifier</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <button class="btn add" @click="openModal('addReferent')">Ajouter</button>

      <!-- Directeur -->
      <h2 class="title">Directeur</h2>
      <div class="table-container">
        <table class="custom-table">
          <thead>
            <tr>
              <th>Nom</th>
              <th>Prénom</th>
              <th>Email</th>
              <th>Modifier</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Defossez</td>
              <td>Adrien</td>
              <td>adrien.defossez@univ-jfc.fr</td>
              <td>
                <button class="btn delete" @click="deleteDirector">🗑</button>
                <button class="btn edit" @click="openModal('editDirector')">Modifier</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <button class="btn add" @click="openModal('addDirector')">Ajouter directeur temporaire</button>
    </div>

    <!-- Modals -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h3>{{ modalTitle }}</h3>

        <label v-if="modalType !== 'editDirector'">Email</label>
        <input v-if="modalType !== 'editDirector'" type="text" v-model="form.email" />

        <label>Nom</label>
        <input type="text" v-model="form.name" />

        <label>Prénom</label>
        <input type="text" v-model="form.surname" />

        <button class="btn confirm" @click="saveData">Confirmer</button>
        <button class="btn cancel" @click="closeModal">Annuler</button>
      </div>
    </div>
  
</template>

<script setup>
import { ref } from "vue";

// Liste des référents
const referents = ref([
  { id: 1, name: "Jean-marie Pécatte", activity: "Nuit de l'info", engagement: "Diversité" },
  { id: 2, name: "Sylvain Barreau", activity: "Main à la pâte", engagement: "Diversité" },
  { id: 3, name: "Fleuranceau Manon", activity: "JPO", engagement: "École" },
  { id: 4, name: "Aurélie Vidallice", activity: "JPO", engagement: "École" },
]);

// Gestion des Modals
const showModal = ref(false);
const modalType = ref("");
const modalTitle = ref("");
const form = ref({ email: "", name: "", surname: "" });

const openModal = (type, data = {}) => {
  modalType.value = type;
  modalTitle.value =
    type === "addReferent" ? "Ajouter un Référent" :
    type === "editReferent" ? "Modifier un Référent" :
    "Directeur";
  form.value = { ...data };
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

const saveData = () => {
  console.log("Données sauvegardées :", form.value);
  closeModal();
};

// Suppression Référent
const deleteReferent = (id) => {
  referents.value = referents.value.filter((r) => r.id !== id);
};

// Suppression Directeur
const deleteDirector = () => {
  alert("Le directeur a été supprimé !");
};
</script>

<style scoped>
/* Contenu principal */
.main-content {
    font-family: 'Inter', sans-serif;
    max-width: 100%;
    margin: 0 auto;
    background-color: #f8f9fa;
}

/* Titre */
.title {
  color: #6A3FA0;
  font-size: 22px;
  margin-bottom: 15px;
}

/* Tables */
.table-container {
  background: white;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 20px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.custom-table {
  width: 100%;
  border-collapse: collapse;
}

.custom-table th,
.custom-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.custom-table th {
  background-color: #f9f9f9;
}

.custom-table tr:hover {
  background-color: #f5f5f5;
}

/* Boutons */
.btn {
  border: none;
  padding: 8px 12px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
}

.btn.delete {
  background-color: #e74c3c;
  color: white;
}

.btn.edit {
  background-color: #ff6b6b;
  color: white;
  margin-left: 5px;
}

.btn.add {
  background-color: #e74c3c;
  color: white;
  padding: 10px 15px;
  margin: 10px 0;
}

/* Modals */
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
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.modal-content h3 {
  margin-bottom: 15px;
  color: #6A3FA0;
}

.modal-content label {
  display: block;
  margin-bottom: 5px;
  color: #333;
}

.modal-content input {
  width: 100%;
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-content .btn.confirm {
  background-color: #6A3FA0;
  color: white;
  margin-right: 10px;
}

.modal-content .btn.cancel {
  background-color: #e74c3c;
  color: white;
}
</style>