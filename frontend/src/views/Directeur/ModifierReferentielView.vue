<template>
  <div class="main-layout">
    <!-- Sidebar -->
    <div class="sidebar">
      <button class="sidebar-button" @click="setActiveEngagement('associatif')">
         Engagement Associatif<span class="icon">✏️</span>
      </button>
      <button class="sidebar-button" @click="setActiveEngagement('ecole')">
         Engagement Ecole<span class="icon">✏️</span>
      </button>
      <button class="sidebar-button" @click="setActiveEngagement('climat')">
         Engagement Climat-Environnement<span class="icon">✏️</span>
      </button>
      <button class="sidebar-button" @click="setActiveEngagement('diversite')">
         Engagement Diversité<span class="icon">✏️</span>
      </button>
      <button class="sidebar-button add-engagement" @click="openEngagementModal">
        <span class="icon">➕</span> Ajouter un Engagement
      </button>
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <!-- Référentiels -->
      <div class="referentiels-container">
        <h2>{{ activeEngagement }}</h2>
        <div class="referentiel-card">
          <h3>Activités</h3>
          <div class="activity-list">
            <div class="activity-item" v-for="(activity, index) in activities" :key="activity">
              {{ activity }}
              <div class="activity-actions">
                <span class="icon" @click="editActivity(index)">✏️</span>
                <span class="icon" @click="deleteActivity(index)">🗑️</span>
              </div>
            </div>
          </div>
          <button class="btn-add-activity" @click="openActivityModal">
            <span class="icon">➕</span> Ajouter une Activité
          </button>
        </div>
      </div>
    </div>

    <!-- Modal pour ajouter/modifier un engagement -->
    <div v-if="showEngagementModal" class="modal-overlay">
      <div class="modal-content">
        <h3>Ajouter un Engagement</h3>
        <form @submit.prevent="saveEngagement">
          <div class="form-group">
            <label for="engagementType">Type d'Engagement</label>
            <input type="text" id="engagementType" v-model="engagement.type" required />
          </div>
          <div class="form-group">
            <label for="engagementDescription">Description</label>
            <textarea id="engagementDescription" v-model="engagement.description" required></textarea>
          </div>
          <div class="form-group">
            <label for="engagementResponsable">Responsable</label>
            <input type="text" id="engagementResponsable" v-model="engagement.responsable" required />
          </div>
          <div class="form-actions">
            <button type="button" class="btn-cancel" @click="closeEngagementModal">Annuler</button>
            <button type="submit" class="btn-confirm">Confirmer</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal pour ajouter/modifier une activité -->
    <div v-if="showActivityModal" class="modal-overlay">
      <div class="modal-content">
        <h3>Ajouter une Activité</h3>
        <form @submit.prevent="saveActivity">
          <div class="form-group">
            <label for="activityName">Nom de l'Activité</label>
            <input type="text" id="activityName" v-model="activity.name" required />
          </div>
          <div class="form-group">
            <label for="activityDescription">Description</label>
            <textarea id="activityDescription" v-model="activity.description" required></textarea>
          </div>
          <div class="form-actions">
            <button type="button" class="btn-cancel" @click="closeActivityModal">Annuler</button>
            <button type="submit" class="btn-confirm">Confirmer</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

// Gestion des modals
const showEngagementModal = ref(false);
const showActivityModal = ref(false);
const engagement = ref({
  type: "",
  description: "",
  responsable: "",
});
const activity = ref({
  name: "",
  description: "",
});

const activeEngagement = ref("Engagement Associatif");
const activities = ref(["MC", "BDE", "JPO"]);

const setActiveEngagement = (engagement) => {
  activeEngagement.value = `Engagement ${engagement.charAt(0).toUpperCase() + engagement.slice(1)}`;
  // Mettre à jour les activités en fonction de l'engagement sélectionné
  switch (engagement) {
    case 'associatif':
      activities.value = ["MC", "BDE", "JPO"];
      break;
    case 'ecole':
      activities.value = ["Activité 1", "Activité 2"];
      break;
    case 'climat':
      activities.value = ["Activité 3", "Activité 4"];
      break;
    case 'diversite':
      activities.value = ["Activité 5", "Activité 6"];
      break;
    default:
      activities.value = [];
  }
};

const openEngagementModal = () => {
  showEngagementModal.value = true;
};

const closeEngagementModal = () => {
  showEngagementModal.value = false;
  engagement.value = { type: "", description: "", responsable: "" };
};

const saveEngagement = () => {
  console.log("Engagement ajouté :", engagement.value);
  closeEngagementModal();
};

const openActivityModal = () => {
  showActivityModal.value = true;
};

const closeActivityModal = () => {
  showActivityModal.value = false;
  activity.value = { name: "", description: "" };
};

const saveActivity = () => {
  console.log("Activité ajoutée :", activity.value);
  activities.value.push(activity.value.name);
  closeActivityModal();
};

const editActivity = (index) => {
  const activityName = activities.value[index];
  activity.value.name = activityName;
  openActivityModal();
};

const deleteActivity = (index) => {
  activities.value.splice(index, 1);
};
</script>

<style scoped>
/* Layout principal */
.main-layout {
  display: flex;
  min-height: 100vh;
  background-color: #f8f9fa;
}

.sidebar {
  width: 250px;
  height: 550px;
  background-color: rgb(248, 243, 253);
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.sidebar-button {
  display: flex;
  align-items: center;
  justify-content: space-between; /* Aligne le texte à gauche et l'icône à droite */
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  background-color: rgb(193, 151, 244);
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  text-align: left;
  color: #4a1f80;
}

.sidebar-button:hover {
  background-color: #4a1f80;
  color: #fefefefe;
}

.sidebar-button .icon {
  margin-left: auto; /* Pousse l'icône vers la droite */
  font-size: 16px;
}

.add-engagement {
  margin-top: auto;
}

.add-engagement:hover {
  background-color: #3a0f70;
}

/* Main Content */
.main-content {
  padding: 20px;
    font-family: 'Inter', sans-serif;
    max-width: 1300px;
    margin: 0 auto;
    background-color: #f8f9fa;
}

/* Référentiels */
.referentiels-container {
  margin-top: 20px;
}

.referentiels-container h2 {
  color: #6a3fa0;
  font-size: 22px;
  margin-bottom: 15px;
}

.referentiel-card {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.referentiel-card h3 {
  font-size: 18px;
  margin-bottom: 10px;
  color: #333;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.activity-item {
  background-color: #f1f1f1;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  font-size: 14px;
  color: #555;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.activity-actions {
  display: flex;
  gap: 10px;
}

.icon {
  cursor: pointer;
  font-size: 16px;
}

.btn-add-activity {
  background-color: #6a3fa0;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
  margin-top: 20px;
  margin-left: auto; /* Déplace le bouton à droite */
  display: block; /* Assure que le bouton occupe toute la largeur disponible */
  justify-content: space-between;
  align-items: center;
}

.btn-add-activity:hover {
  background-color: #5a2f90;
}

/* Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.modal-content h3 {
  font-size: 20px;
  margin-bottom: 20px;
  color: #6a3fa0;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #333;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.btn-cancel {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.btn-cancel:hover {
  background-color: #c0392b;
}

.btn-confirm {
  background-color: #6a3fa0;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.btn-confirm:hover {
  background-color: #5a2f90;
}
</style>