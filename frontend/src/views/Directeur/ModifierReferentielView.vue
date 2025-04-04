<template>
  <div class="main-layout">
    <!-- Sidebar : Liste dynamique des engagements -->
    <div class="sidebar">
      <div v-for="ref in referentiels" :key="ref.idReferentiel">
        <button
          class="sidebar-button"
          :class="{ active: activeReferentiel && activeReferentiel.idReferentiel === ref.idReferentiel }"
          @click="selectEngagement(ref.idReferentiel)"
        >
          {{ ref.nom }}
          <span class="icon">✏️</span>
        </button>
      </div>
      <button class="sidebar-button add-engagement" @click="openEngagementModal">
        <span class="icon">➕</span> Ajouter un Engagement
      </button>
      <button class="sidebar-button manage-engagement" @click="openManageEngagementsModal">
        <span class="icon">⚙️</span> Modifier les Engagements
      </button>
    </div>

    <!-- Main Content : Affichage du référentiel actif et de ses actions -->
    <div class="main-content" v-if="activeReferentiel">
      <h2>{{ activeReferentiel.nom }}</h2>
      <p>{{ activeReferentiel.description }}</p>
      <div class="referentiel-card">
        <h3>Actions</h3>
        <div class="activity-list">
          <div class="activity-item" v-for="(act, index) in activeReferentiel.actions" :key="act.idAction || index">
            {{ act.nom }}
            <div class="activity-actions">
              <span class="icon" @click="editAction(act)">✏️</span>
              <span class="icon" @click="deleteAction(act.idAction)">🗑️</span>
            </div>
          </div>
        </div>
        <button class="btn-add-activity" @click="openActionModal">
          <span class="icon">➕</span> Ajouter une Action
        </button>
      </div>
    </div>
    <div class="main-content" v-else>
      <p>Aucun engagement sélectionné.</p>
    </div>

    <!-- Modal Engagement (ajouter/modifier) -->
    <div v-if="showEngagementModal" class="modal-overlay">
      <div class="modal-content">
        <h3>{{ engagement.idReferentiel ? "Modifier" : "Ajouter" }} un Engagement</h3>
        <form @submit.prevent="saveEngagement">
          <div class="form-group">
            <label for="engagementName">Nom de l'Engagement</label>
            <input type="text" id="engagementName" v-model="engagement.nom" required />
          </div>
          <div class="form-group">
            <label for="engagementDescription">Description</label>
            <textarea id="engagementDescription" v-model="engagement.description" required></textarea>
          </div>
          <div class="form-actions">
            <button type="button" class="btn-cancel" @click="closeEngagementModal">Annuler</button>
            <button type="submit" class="btn-confirm">Confirmer</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal Action (ajouter/modifier) -->
    <div v-if="showActionModal" class="modal-overlay">
      <div class="modal-content">
        <h3>{{ action.idAction ? "Modifier" : "Ajouter" }} une Action</h3>
        <form @submit.prevent="saveAction">
          <div class="form-group">
            <label for="actionName">Nom de l'Action</label>
            <input type="text" id="actionName" v-model="action.nom" required />
          </div>
          <div class="form-group">
            <label for="actionDescription">Description</label>
            <textarea id="actionDescription" v-model="action.descriptionAction" required></textarea>
          </div>
          <div class="form-actions">
            <button type="button" class="btn-cancel" @click="closeActionModal">Annuler</button>
            <button type="submit" class="btn-confirm">Confirmer</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal pour gérer l'ensemble des engagements (Modification/Suppression) -->
    <div v-if="showManageEngagementsModal" class="modal-overlay">
      <div class="modal-content wide">
        <h3>Modifier / Supprimer les Engagements</h3>
        <ul class="engagement-list">
          <li v-for="ref in referentiels" :key="ref.idReferentiel" class="engagement-item">
            <span>{{ ref.nom }}</span>
            <div class="engagement-actions">
              <button @click="editEngagement(ref)">Modifier</button>
              <button @click="deleteEngagement(ref.idReferentiel)">Supprimer</button>
            </div>
          </li>
        </ul>
        <div class="form-actions">
          <button type="button" class="btn-cancel" @click="closeManageEngagementsModal">Fermer</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

// Tableaux et variables
const referentiels = ref([]); // Tous les engagements (referentiels) chargés depuis le backend
const activeReferentiel = ref(null);

const showEngagementModal = ref(false);
const showActionModal = ref(false);
const showManageEngagementsModal = ref(false);

// Objet engagement pour modal d'ajout/modification d'engagement
const engagement = ref({
  idReferentiel: null,
  nom: "",
  description: ""
});

// Objet action pour modal d'ajout/modification d'action
const action = ref({
  idAction: null,
  nom: "",
  descriptionAction: ""
});

// Chargement de tous les référentiels depuis le backend
const fetchReferentiels = async () => {
  try {
    const res = await fetch("http://localhost:8989/api/referentiels");
    if (!res.ok) {
      throw new Error("Erreur lors de la récupération des engagements");
    }
    const data = await res.json();
    referentiels.value = data;
    // Par défaut, on définit le premier comme actif
    if (data.length > 0) {
      activeReferentiel.value = data[0];
    }
  } catch (error) {
    console.error("Erreur fetchReferentiels:", error);
  }
};

onMounted(() => {
  fetchReferentiels();
});

// Lorsqu'on clique sur un engagement dans la sidebar, on récupère les détails mis à jour via GET /api/referentiels/{id}
const selectEngagement = async (id) => {
  try {
    const res = await fetch(`http://localhost:8989/api/referentiels/${id}`);
    if (!res.ok) {
      throw new Error("Erreur lors de la récupération de l'engagement");
    }
    const data = await res.json();
    activeReferentiel.value = data;
  } catch (error) {
    console.error("Erreur selectEngagement:", error);
  }
};

// Modal Engagement
const openEngagementModal = () => {
  // Pour l'ajout, on réinitialise le formulaire
  engagement.value = { idReferentiel: null, nom: "", description: "" };
  showEngagementModal.value = true;
};

const closeEngagementModal = () => {
  showEngagementModal.value = false;
};

const saveEngagement = async () => {
  try {
    if (!engagement.value.idReferentiel) {
      // Création d'un nouvel engagement
      const res = await fetch("http://localhost:8989/api/referentiels", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          nom: engagement.value.nom,
          description: engagement.value.description
        })
      });
      if (!res.ok) {
        throw new Error("Erreur lors de la création de l'engagement");
      }
      const newEngagement = await res.json();
      referentiels.value.push(newEngagement);
      activeReferentiel.value = newEngagement;
    } else {
      // Modification de l'engagement existant
      const res = await fetch(`http://localhost:8989/api/referentiels/${engagement.value.idReferentiel}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          nom: engagement.value.nom,
          description: engagement.value.description
        })
      });
      if (!res.ok) {
        throw new Error("Erreur lors de la modification de l'engagement");
      }
      const updated = await res.json();
      // Mettre à jour localement la liste des engagements
      const idx = referentiels.value.findIndex(r => r.idReferentiel === updated.idReferentiel);
      if (idx !== -1) {
        referentiels.value[idx] = updated;
      }
      activeReferentiel.value = updated;
    }
    closeEngagementModal();
  } catch (error) {
    console.error("Erreur saveEngagement:", error);
  }
};

// Modal Action
const openActionModal = () => {
  action.value = { idAction: null, nom: "", descriptionAction: "" };
  showActionModal.value = true;
};

const closeActionModal = () => {
  showActionModal.value = false;
};

const saveAction = async () => {
  try {
    if (!action.value.idAction) {
      // Création d'une nouvelle action
      const payload = {
        nom: action.value.nom,
        descriptionAction: action.value.descriptionAction,
        // Lier l'action au référentiel actif
        referentiel: { idReferentiel: activeReferentiel.value.idReferentiel }
      };
      const res = await fetch("http://localhost:8989/api/actions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
      if (!res.ok) {
        throw new Error("Erreur lors de la création de l'action");
      }
      const newAction = await res.json();
      if (!activeReferentiel.value.actions) {
        activeReferentiel.value.actions = [];
      }
      activeReferentiel.value.actions.push(newAction);
    } else {
      // Modification d'une action existante
      const res = await fetch(`http://localhost:8989/api/actions/${action.value.idAction}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          nom: action.value.nom,
          descriptionAction: action.value.descriptionAction,
          referentiel: { idReferentiel: activeReferentiel.value.idReferentiel }
        })
      });
      if (!res.ok) {
        throw new Error("Erreur lors de la modification de l'action");
      }
      const updatedAction = await res.json();
      const idx = activeReferentiel.value.actions.findIndex(a => a.idAction === updatedAction.idAction);
      if (idx !== -1) {
        activeReferentiel.value.actions[idx] = updatedAction;
      }
    }
    closeActionModal();
  } catch (error) {
    console.error("Erreur saveAction:", error);
  }
};

const editAction = (act) => {
  action.value = { ...act };
  showActionModal.value = true;
};

const deleteAction = async (actionId) => {
  try {
    const res = await fetch(`http://localhost:8989/api/actions/${actionId}`, {
      method: "DELETE"
    });
    if (!res.ok) {
      throw new Error("Erreur lors de la suppression de l'action");
    }
    activeReferentiel.value.actions = activeReferentiel.value.actions.filter(a => a.idAction !== actionId);
  } catch (error) {
    console.error("Erreur deleteAction:", error);
  }
};

/* Gestion des engagements depuis la modal "Modifier les Engagements" */
const openManageEngagementsModal = () => {
  showManageEngagementsModal.value = true;
};

const closeManageEngagementsModal = () => {
  showManageEngagementsModal.value = false;
};

const editEngagement = (ref) => {
  engagement.value = {
    idReferentiel: ref.idReferentiel,
    nom: ref.nom,
    description: ref.description
  };
  // Fermer la modal de gestion et ouvrir celle d'édition
  showManageEngagementsModal.value = false;
  showEngagementModal.value = true;
};

const deleteEngagement = async (idRef) => {
  try {
    const res = await fetch(`http://localhost:8989/api/referentiels/${idRef}`, {
      method: "DELETE"
    });
    if (!res.ok) {
      throw new Error("Erreur lors de la suppression de l'engagement");
    }
    referentiels.value = referentiels.value.filter(r => r.idReferentiel !== idRef);
    if (activeReferentiel.value && activeReferentiel.value.idReferentiel === idRef) {
      activeReferentiel.value = referentiels.value.length ? referentiels.value[0] : null;
    }
  } catch (error) {
    console.error("Erreur deleteEngagement:", error);
  }
};
</script>

<style scoped>
/* Layout principal */
.main-layout {
  display: flex;
  min-height: 100vh;
  background-color: #f8f9fa;
}

/* Sidebar */
.sidebar {
  width: 250px;
  background-color: rgb(248, 243, 253);
  padding: 20px;
  display: flex;
  flex-direction: column;
  margin-top: 40px;
}

.sidebar-button {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  background-color: rgb(193, 151, 244);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  text-align: left;
  color: #4a1f80;
}

.sidebar-button.active {
  background-color: #4a1f80;
  color: white;
}

.sidebar-button:hover {
  background-color: #4a1f80;
  color: #fefefefe;
}

.sidebar-button .icon {
  margin-left: auto;
  font-size: 16px;
}

.add-engagement,
.manage-engagement {
  margin-top: auto;
  margin-bottom: 10px;
}

.manage-engagement {
  background-color: #ffd966;
  color: #444;
}

.manage-engagement:hover {
  background-color: #f0c840;
  color: #222;
}

/* Main Content */
.main-content {
  padding: 20px;
  font-family: 'Inter', sans-serif;
  max-width: 1300px;
  margin: 0 auto;
  background-color: #f8f9fa;
}

.referentiels-container {
  margin-top: 20px;
}

.referentiels-container h2 {
  color: #6a3fa0;
  font-size: 22px;
  margin-bottom: 10px;
}

.referentiel-card {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  margin-bottom: 20px;
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
  margin-left: auto;
  display: block;
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
  background-color: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.modal-content.wide {
  width: 500px;
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
}

.btn-confirm:hover {
  background-color: #5a2f90;
}

/* Modal: Liste des engagements */
.engagement-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.engagement-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  border-bottom: 1px solid #ddd;
}

.engagement-actions button {
  margin-left: 5px;
}

.no-engagement {
  font-size: 16px;
  color: #555;
  margin-top: 40px;
  text-align: center;
}
</style>
