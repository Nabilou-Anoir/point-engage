<template>
  <div class="activities-container">
    <!-- Barre de recherche et filtre -->
    <div class="search-filter">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="Rechercher par nom d'activité"
        class="search-input"
      />
    </div>

    <!-- Tableau des activités -->
    <div class="table-container">
      <table class="activities-table">
        <thead>
          <tr>
            <th>Activité</th>
            <th>Date</th>
            <th class="text-right">Statut</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(activity, index) in filteredActivities" :key="index" class="table-row">
            <td @click="showPopup(activity)" class="activity-name">{{ activity.name }}</td>
            <td>{{ activity.date }}</td>
            <td class="text-right">
              <span v-if="activity.points" class="points">{{ activity.points }} points attribués</span>
              <span v-else :class="getStatusClass(activity.status)">{{ activity.status }}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="pagination">
      <button 
        @click="prevPage" 
        :disabled="currentPage === 1" 
        class="pagination-button"
      >
        Précédent
      </button>
      <span class="page-indicator">Page {{ currentPage }} sur {{ totalPages }}</span>
      <button 
        @click="nextPage" 
        :disabled="currentPage === totalPages" 
        class="pagination-button"
      >
        Suivant
      </button>
    </div>

    <!-- Popup -->
    
<div v-if="selectedActivity" class="modal-overlay" @click="closePopup">
  <div class="modal-content" @click.stop>
    <h2>{{ selectedActivity.name }}</h2>
    <p><strong>Date:</strong> {{ selectedActivity.date }}</p>
    <p><strong>Statut:</strong> {{ selectedActivity.status || 'Points attribués: ' + selectedActivity.points }}</p>

    <!-- Étapes en une seule ligne -->
    <div class="steps-container">
  <div class="step">
    <div class="step-header">
      <div class="step-number">1</div>
      <div class="step-label">Fiche envoyée</div>
    </div>
    <div class="step-description">Votre fiche est bien partie ! Elle est en cours d’examen par votre référent.</div>
  </div>
  <div class="step-connector"></div>
  <div class="step">
    <div class="step-header">
      <div class="step-number">2</div>
      <div class="step-label">Validation en cours</div>
    </div>
    <div class="step-description">Votre référent analyse vos activités et peut ajouter des remarques si besoin.</div>
  </div>
  <div class="step-connector"></div>
  <div class="step">
    <div class="step-header">
      <div class="step-number">3</div>
      <div class="step-label">Transmission au directeur</div>
    </div>
    <div class="step-description">Félicitations ! Votre fiche a été validée et transmise au directeur pour l’attribution des points.</div>
  </div>
  <div class="step-connector"></div>
  <div class="step">
    <div class="step-header">
      <div class="step-number">4</div>
      <div class="step-label">Points attribués</div>
    </div>
    <div class="step-description">Votre engagement est reconnu ! Consultez vos points et poursuivez votre parcours.</div>
  </div>
</div>

    <button @click="closePopup" class="modal-close-button">Fermer</button>
  </div>
</div>
  </div>
</template>

<script>
export default {
  name: 'ActivitiesView',
  data() {
    return {
      activities: [
        { name: 'Activité 1', date: '28/01/2025', points: '0.2' },
        { name: 'Activité 2', date: '01/02/2025', points: '0.2' },
        { name: 'Activité 3', date: '02/02/2025', status: 'Validé par le référent' },
        { name: 'Activité 4', date: '05/02/2025', status: 'En attente de validation' },
        // Ajoutez plus d'activités ici pour tester la pagination
      ],
      searchQuery: '',
      currentPage: 1,
      itemsPerPage: 3,
      selectedActivity: null
    };
  },
  computed: {
    filteredActivities() {
      return this.activities
        .filter(activity =>
          activity.name.toLowerCase().includes(this.searchQuery.toLowerCase())
        )
        .slice((this.currentPage - 1) * this.itemsPerPage, this.currentPage * this.itemsPerPage);
    },
    totalPages() {
      return Math.ceil(this.activities.length / this.itemsPerPage);
    }
  },
  methods: {
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
    getStatusClass(status) {
      switch (status) {
        case 'Validé par le référent':
          return 'status-valid';
        case 'En attente de validation':
          return 'status-pending';
        default:
          return 'status-default';
      }
    },
    showPopup(activity) {
      this.selectedActivity = activity;
    },
    closePopup() {
      this.selectedActivity = null;
    }
  }
};
</script>

<style scoped>
/* Styles globaux */
body {
  font-family: 'Inter', sans-serif;
  background: #f4f4f9;
  margin: 0;
  padding: 0;
}

/* Conteneur principal */
.activities-container {
  max-width: 1300px;
  margin: 40px auto;
  padding: 40px;
  background: #ffffff;
  border-radius: 20px;
  box-shadow: 0 15px 40px rgba(217, 199, 199, 0.12);
  animation: fadeIn 0.5s ease-in-out;
}

/* Barre de recherche */
.search-filter {
  margin-bottom: 30px;
}

.search-input {
  width: 30%;
  padding: 12px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 7px;
  font-size: 1rem;
  color: #333;
  background: #f9f9f9;
  transition: all 0.3s ease;
}

.search-input:focus {
  border-color: #6A3FA0;
  box-shadow: 0 0 12px rgba(106, 63, 160, 0.3);
  outline: none;
}

/* Tableau */
.table-container {
  overflow-x: auto;
  border-radius: 7px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.activities-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.activities-table th, .activities-table td {
  padding: 16px 20px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.activities-table th {
  
  color: #666;
  font-weight: 600;
  text-transform: uppercase;
  font-size: 0.7rem;
}

.activities-table th.text-right,
.activities-table td.text-right {
  text-align: right;
}

.activities-table tbody tr {
  transition: background-color 0.3s ease;
}

.activities-table tbody tr:hover {
  background-color: #f8f8f8;
 
}

.activity-name {
  cursor: pointer;
  color: #6A3FA0;
  font-weight: 500;
  text-decoration: underline;
}

/* Styles pour les statuts et points */
.points {
  color: #4CAF50;
  font-weight: 500;
  padding: 6px 12px;
  border-radius: 7px;
  background-color: #e8f5e9;
}

.status-valid {
  color: #4CAF50;
  font-weight: 500;
  padding: 6px 12px;
  border-radius: 7px;
  background-color: #e8f5e9;
}

.status-pending {
  color: #FFA000;
  font-weight: 500;
  padding: 6px 12px;
  border-radius: 7px;
  background-color: #fff3e0;
}

.status-default {
  color: #333;
  font-weight: 500;
  padding: 6px 12px;
  border-radius: 7px;
  background-color: #f5f5f5;
}

/* Pagination */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-top: 30px;
}

.pagination-button {
  padding: 10px 20px;
  border: none;
  border-radius: 12px;
  background-color: #6A3FA0;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.pagination-button:disabled {
  background-color: #e0e0e0;
  cursor: not-allowed;
}

.pagination-button:hover:not(:disabled) {
  background-color: #5a2f8f;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(106, 63, 160, 0.3);
}

.page-indicator {
  font-size: 1rem;
  color: #333;
}

/* Popup */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6); /* Fond plus sombre pour plus de contraste */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(5px); /* Effet de flou pour l'arrière-plan */
  animation: fadeIn 0.3s ease-in-out;
}

.modal-content {
  background: white;
  padding: 40px;
  border-radius: 16px;
  max-width: 900px;
  width: 90%;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  transform: scale(0.95);
  opacity: 0;
  animation: popIn 0.3s ease-in-out forwards;
}

@keyframes popIn {
  to {
    transform: scale(1);
    opacity: 1;
  }
}

.modal-content h2 {
  margin-bottom: 20px;
  color: #6A3FA0;
  font-size: 1.8rem;
  font-weight: 700;
}

.modal-content p {
  margin-bottom: 15px;
  font-size: 1rem;
  color: #555;
}

.modal-content strong {
  color: #333;
}

/* Étapes en une seule ligne */
.steps-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-top: 30px;
  position: relative;
}

.step {
  text-align: center; /* Alignement à gauche */
  position: relative;
  flex: 1;
  padding: 0 10px;
}

.step-header {
  display: flex;
  align-items: center; /* Alignement vertical des éléments */
  gap: 10px; /* Espace entre le numéro et le titre */
  margin-bottom: 10px;
}

.step-number {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #6A3FA0;
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: 700;
  font-size: 0.8rem;
  box-shadow: 0 4px 10px rgba(106, 63, 160, 0.3);
  transition: all 0.3s ease;
  flex-shrink: 0; /* Empêche le numéro de rétrécir */
}

.step-label {
  font-weight: 600;
  color: #6A3FA0;
  font-size: 0.8rem;
  transition: color 0.3s ease;
}

.step-description {
  font-size: 0.7rem;
  color: #777;
  line-height: 1.4;
  margin-top: 10px; 
}

.step-connector {
  flex: 1;
  height: 2px;
  background: #6A3FA0;
  margin: 0 10px;
  position: relative;
  top: 20px;
  opacity: 0.3;
}

/* Bouton de fermeture */
.modal-close-button {
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  background-color: #6A3FA0;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 30px;
  font-size: 1rem;
  box-shadow: 0 4px 10px rgba(106, 63, 160, 0.3);
}

.modal-close-button:hover {
  background-color: #5a2f8f;
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(106, 63, 160, 0.4);
}

/* Animation */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>