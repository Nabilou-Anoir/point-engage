<!-- src/views/ProfilView.vue -->
<template>
  <div class="profil-container">
    <h2>Profil</h2>

    <!-- Sélection de l'étudiant -->
    <div class="form-group">
      <label>Étudiant <span class="required">*</span></label>
      <select v-model="selectionEtudiant" @change="fetchProfil">
        <option disabled value="">-- Sélectionnez un étudiant --</option>
        <option v-for="et in etudiants" :key="et.idEtudiant" :value="et.idEtudiant">
          {{ et.nom }} {{ et.prenom }}
        </option>
      </select>
    </div>

    <!-- Affichage du profil de l'étudiant -->
    <div v-if="profilDisponible" class="profil-card">
      <h3>Profil de {{ profil.prenom }} {{ profil.nom }}</h3>

      <!-- Si on n'est pas en mode édition, on affiche simplement les données -->
      <div v-if="!editMode">
        <p><strong>Promotion :</strong> {{ profil.promotion }}</p>
        <p><strong>Email :</strong> {{ profil.email }}</p>

        <button class="btn-edit" @click="enterEditMode">
          Modifier le profil
        </button>
      </div>

      <!-- Mode édition : champs modifiables -->
      <div v-else class="edit-section">
        <div class="edit-field">
          <label for="promotion">Promotion :</label>
          <input
            id="promotion"
            v-model="editPromotion"
            type="text"
            placeholder="Modifier la promotion"
          />
        </div>

        <div class="edit-field">
          <label for="email">Email :</label>
          <input
            id="email"
            v-model="editEmail"
            type="email"
            placeholder="Modifier l'email"
          />
        </div>

        <div class="edit-actions">
          <button class="btn-save" @click="saveChanges">
            Enregistrer
          </button>
          <button class="btn-cancel" @click="cancelEdit">
            Annuler
          </button>
        </div>
      </div>
    </div>

    <!-- Aucune sélection -->
    <div v-else class="profil-card">
      <p>Aucun profil sélectionné.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

// Liste des étudiants et sélection
const etudiants = ref([])
const selectionEtudiant = ref('')

// Informations du profil affiché
const profil = ref({})

// État du mode édition
const editMode = ref(false)

// Champs d'édition (on les sépare pour ne pas modifier directement profil)
const editPromotion = ref('')
const editEmail = ref('')

// Détermine si un profil est disponible
const profilDisponible = computed(() => {
  return profil.value && profil.value.idEtudiant
})

// URL de base pour les étudiants
const ETUDIANTS_URL = 'http://localhost:8989/api/etudiants'

// Récupère la liste des étudiants
function getEtudiants() {
  fetch(ETUDIANTS_URL)
    .then(response => response.json())
    .then(data => {
      etudiants.value = data
    })
    .catch(error =>
      console.error("Erreur lors du chargement des étudiants :", error)
    )
}

// Récupère le profil de l'étudiant sélectionné
function fetchProfil() {
  const id = selectionEtudiant.value
  const selected = etudiants.value.find(e => e.idEtudiant == id)
  if (selected) {
    profil.value = { ...selected }
    // Initialiser les champs d'édition
    editPromotion.value = profil.value.promotion
    editEmail.value = profil.value.email
    editMode.value = false
  } else {
    profil.value = {}
    editMode.value = false
  }
}

// Active le mode édition
function enterEditMode() {
  editMode.value = true
}

// Annule les modifications
function cancelEdit() {
  // On réinitialise les champs avec la valeur actuelle du profil
  editPromotion.value = profil.value.promotion
  editEmail.value = profil.value.email
  editMode.value = false
}

// Enregistre les modifications (PUT)
function saveChanges() {
  // Met à jour l'objet profil
  profil.value.promotion = editPromotion.value
  profil.value.email = editEmail.value

  // Effectue la requête PUT pour mettre à jour l'étudiant
  const url = `${ETUDIANTS_URL}/${profil.value.idEtudiant}`
  fetch(url, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(profil.value)
  })
    .then(response => {
      if (!response.ok) {
        throw new Error(`Erreur HTTP : ${response.status}`)
      }
      return response.json()
    })
    .then(updatedData => {
      // Mettre à jour localement si besoin
      profil.value = updatedData
      // Met à jour la liste globale (facultatif, selon votre API)
      const index = etudiants.value.findIndex(e => e.idEtudiant === updatedData.idEtudiant)
      if (index !== -1) {
        etudiants.value[index] = updatedData
      }
      editMode.value = false
    })
    .catch(error => {
      console.error("Erreur lors de la mise à jour de l'étudiant :", error)
      // Vous pouvez afficher un message d'erreur à l'utilisateur
    })
}

onMounted(() => {
  getEtudiants()
})
</script>

<style scoped>
.profil-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.form-group {
  margin-bottom: 1rem;
}

.required {
  color: red;
}

.profil-card {
  margin-top: 1.5rem;
  background-color: #f7f7f7;
  padding: 15px;
  border-radius: 8px;
  border: 1px solid #ddd;
}

.edit-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.edit-field {
  display: flex;
  flex-direction: column;
}

.edit-field label {
  font-weight: bold;
  margin-bottom: 0.25rem;
}

.edit-actions {
  display: flex;
  gap: 10px;
  margin-top: 1rem;
}

.btn-edit,
.btn-save,
.btn-cancel {
  background-color: #6C3EA9;
  color: #fff;
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.btn-edit:hover,
.btn-save:hover,
.btn-cancel:hover {
  background-color: #532a80;
}
</style>
