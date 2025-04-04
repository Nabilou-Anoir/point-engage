<template>
  <div class="main-content">
    <!-- Référents -->
    <h2 class="title">Référents</h2>
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
        <tr v-for="referent in referents" :key="referent.id">
          <td>{{ referent.nom }}</td>
          <td>{{ referent.prenom }}</td>
          <td>{{ referent.email }}</td>
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

    <!-- Modals -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h3>{{ modalTitle }}</h3>

        <!-- Pour un référent, on affiche Email, Nom et Prénom -->
        <div v-if="modalType !== 'editDirector'">
          <label>Email</label>
          <input type="text" v-model="form.email" />
        </div>

        <label>Nom</label>
        <input type="text" v-model="form.nom" />

        <label>Prénom</label>
        <input type="text" v-model="form.prenom" />

        <button class="btn confirm" @click="saveData">Confirmer</button>
        <button class="btn cancel" @click="closeModal">Annuler</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

// Liste des référents récupérée depuis le backend
const referents = ref([]);

const showModal = ref(false);
const modalType = ref("");
const modalTitle = ref("");
// Attention, ici on adapte le modèle aux propriétés de l'entité backend (nom, prenom, email)
const form = ref({ id: null, email: "", nom: "", prenom: "" });

// Chargement initial des référents via API
const fetchReferents = async () => {
  try {
    const response = await axios.get("/api/referents");
    // On suppose que le backend renvoie des objets avec les attributs nom, prenom et email
    referents.value = response.data;
  } catch (error) {
    console.error("Erreur lors du chargement des référents :", error);
  }
};

onMounted(fetchReferents);

// Gestion de l'ouverture d'une modal
const openModal = (type, data = {}) => {
  modalType.value = type;
  modalTitle.value =
    type === "addReferent" ? "Ajouter un Référent" :
      type === "editReferent" ? "Modifier un Référent" :
        "Directeur";
  // En cas d'édition, on remplit le formulaire avec les données existantes
  form.value = { ...data };
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

// Sauvegarde des données en fonction du type d'opération
const saveData = async () => {
  try {
    if (modalType.value === "addReferent") {
      // Appel POST pour ajouter un référent
      const response = await axios.post("/api/referents", form.value);
      referents.value.push(response.data);
    } else if (modalType.value === "editReferent") {
      // Appel PUT pour modifier un référent existant
      const response = await axios.put(`/api/referents/${form.value.id}`, form.value);
      // Mise à jour locale de la liste
      const index = referents.value.findIndex(r => r.id === form.value.id);
      if (index !== -1) {
        referents.value[index] = response.data;
      }
    }
    closeModal();
  } catch (error) {
    console.error("Erreur lors de la sauvegarde :", error);
  }
};

// Suppression d'un référent via appel DELETE
const deleteReferent = async (id) => {
  try {
    await axios.delete(`/api/referents/${id}`);
    referents.value = referents.value.filter(r => r.id !== id);
  } catch (error) {
    console.error("Erreur lors de la suppression :", error);
  }
};

// Méthode factice pour la suppression du directeur (à adapter ultérieurement)
const deleteDirector = () => {
  alert("Le directeur a été supprimé !");
};
</script>

<style scoped>
/* (Utilisez le même style que dans votre template initial) */
.main-content {
  font-family: 'Inter', sans-serif;
  max-width: 100%;
  margin: 0 auto;
  background-color: #f8f9fa;
}

.title {
  color: #6A3FA0;
  font-size: 22px;
  margin-bottom: 15px;
}

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
