<template>
  <div class="main-content">
    <!-- Section Référents -->
    <h2 class="title">Référents</h2>
    <div class="table-container">
      <table class="custom-table">
        <thead>
        <tr>
          <th>Nom</th>
          <th>Prénom</th>
          <th>Email</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <!-- On utilise referent.idReferent comme clé -->
        <tr v-for="referent in referents" :key="referent.idReferent">
          <td>{{ referent.nom }}</td>
          <td>{{ referent.prenom }}</td>
          <td>{{ referent.email }}</td>
          <td>
            <button class="btn delete" @click="deleteReferent(referent.idReferent)">🗑</button>
            <button class="btn edit" @click="openModal('editReferent', referent)">Modifier</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <button class="btn add" @click="openModal('addReferent')">Ajouter Référent</button>

    <!-- Section Directeur -->
    <h2 class="title">Directeur</h2>
    <div class="table-container">
      <table class="custom-table">
        <thead>
        <tr>
          <th>Nom</th>
          <th>Prénom</th>
          <th>Email</th>
          <th>Username</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="dir in directors" :key="dir.idUser">
          <td>{{ dir.nom }}</td>
          <td>{{ dir.prenom }}</td>
          <td>{{ dir.email }}</td>
          <td>{{ dir.username }}</td>
          <td>
            <button class="btn delete" @click="deleteDirector(dir.idUser)">🗑</button>
            <button class="btn edit" @click="openModal('editDirector', dir)">Modifier</button>
          </td>
        </tr>
        <tr v-if="directors.length === 0">
          <td colspan="5">Aucun directeur enregistré</td>
        </tr>
        </tbody>
      </table>
    </div>
    <button class="btn add" @click="openModal('addDirector')">Ajouter Directeur</button>

    <!-- Modal commun pour Référent et Directeur -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h3>{{ modalTitle }}</h3>
        <!-- Si on gère un directeur, on affiche email, username, password, nom et prénom -->
        <div v-if="isDirectorModal">
          <label>Email</label>
          <input type="text" v-model="form.email" />
          <label>Username</label>
          <input type="text" v-model="form.username" />
          <label>Password</label>
          <input type="password" v-model="form.password" />
          <label>Nom</label>
          <input type="text" v-model="form.nom" />
          <label>Prénom</label>
          <input type="text" v-model="form.prenom" />
        </div>
        <!-- Sinon, pour un référent, on affiche email, nom et prénom -->
        <div v-else>
          <label>Email</label>
          <input type="text" v-model="form.email" />
          <label>Nom</label>
          <input type="text" v-model="form.nom" />
          <label>Prénom</label>
          <input type="text" v-model="form.prenom" />
        </div>
        <button class="btn confirm" @click="saveData">Confirmer</button>
        <button class="btn cancel" @click="closeModal">Annuler</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

// États pour la section Référents
const referents = ref([]);

// États pour la section Directeur
const directors = ref([]);

// États de gestion de la modal
const showModal = ref(false);
const modalType = ref('');
const modalTitle = ref('');
// Objet formulaire adapté selon le type (référent ou directeur)
const form = ref({});

// Computed pour déterminer si la modal concerne un directeur
const isDirectorModal = computed(() =>
  modalType.value === 'addDirector' || modalType.value === 'editDirector'
);

// Chargement des référents depuis le backend
const fetchReferents = async () => {
  try {
    const response = await axios.get('/api/referents');
    referents.value = response.data;
  } catch (error) {
    console.error("Erreur lors du chargement des référents :", error);
  }
};

// Chargement des directeurs via l'endpoint dédié
const fetchDirectors = async () => {
  try {
    const response = await axios.get('/api/utilisateurs/directeurs');
    directors.value = response.data;
  } catch (error) {
    console.error("Erreur lors du chargement des directeurs :", error);
    directors.value = [];
  }
};

onMounted(() => {
  fetchReferents();
  fetchDirectors();
});

// Ouverture de la modal selon le type d'opération
const openModal = (type, data = {}) => {
  modalType.value = type;
  if (type === 'addReferent') {
    modalTitle.value = 'Ajouter un Référent';
    form.value = { idReferent: null, email: '', nom: '', prenom: '' };
  } else if (type === 'editReferent') {
    modalTitle.value = 'Modifier un Référent';
    form.value = { ...data };
  } else if (type === 'addDirector') {
    modalTitle.value = 'Ajouter un Directeur';
    // Formulaire vide pour l'ajout d'un directeur
    form.value = { idUser: null, email: '', username: '', password: '', nom: '', prenom: '', promotion: '' };
  } else if (type === 'editDirector') {
    modalTitle.value = 'Modifier le Directeur';
    form.value = { ...data };
  }
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

// Sauvegarde selon le type de modal
const saveData = async () => {
  try {
    if (modalType.value === 'addReferent') {
      // Ajout d'un référent
      const response = await axios.post('/api/referents', form.value);
      referents.value.push(response.data);
    } else if (modalType.value === 'editReferent') {
      // Modification d'un référent
      const response = await axios.put(`/api/referents/${form.value.idReferent}`, form.value);
      const index = referents.value.findIndex(r => r.idReferent === form.value.idReferent);
      if (index !== -1) {
        referents.value[index] = response.data;
      }
    } else if (modalType.value === 'addDirector') {
      // Ajout d'un directeur via l'endpoint dédié
      await axios.post('/auth/registerDirector', form.value);
      await fetchDirectors();
    } else if (modalType.value === 'editDirector') {
      // Modification d'un directeur via l'endpoint de mise à jour
      const response = await axios.put(`/api/utilisateurs/${form.value.idUser}`, form.value);
      const index = directors.value.findIndex(d => d.idUser === form.value.idUser);
      if (index !== -1) {
        directors.value[index] = response.data;
      }
    }
    closeModal();
  } catch (error) {
    console.error("Erreur lors de la sauvegarde :", error);
  }
};

// Suppression d'un référent
const deleteReferent = async (idReferent) => {
  try {
    await axios.delete(`/api/referents/${idReferent}`);
    referents.value = referents.value.filter(r => r.idReferent !== idReferent);
  } catch (error) {
    console.error("Erreur lors de la suppression d'un référent :", error);
  }
};

// Suppression d'un directeur
const deleteDirector = async (idUser) => {
  try {
    await axios.delete(`/api/utilisateurs/${idUser}`);
    directors.value = directors.value.filter(d => d.idUser !== idUser);
  } catch (error) {
    console.error("Erreur lors de la suppression d'un directeur :", error);
  }
};
</script>

<style scoped>
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
