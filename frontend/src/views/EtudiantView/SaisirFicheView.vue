<template>
  <div class="fiche-form-container">
    <h2>Fiche descriptive Ingénieur Engagé</h2>
    <form @submit.prevent="submitParticipation" class="fiche-form">
      <!-- Sélection de l'étudiant -->
      <div class="form-group">
        <label>Étudiant <span class="required">*</span></label>
        <select v-model="selectionEtudiant" required>
          <option disabled value="">-- Sélectionnez votre nom et prénom --</option>
          <option v-for="et in etudiants" :key="et.idEtudiant" :value="et.idEtudiant">
            {{ et.nom }} {{ et.prenom }}
          </option>
        </select>
      </div>


      <!-- Sélection du semestre -->
      <div class="form-group">
        <label>Semestre <span class="required">*</span></label>
        <select v-model="selectionSemestre" required>
          <option disabled value="">-- Sélectionnez un semestre --</option>
          <option v-for="sem in semestres" :key="sem.idSemestre" :value="sem.idSemestre">
            S{{ sem.nbSemestre }}
          </option>
        </select>
      </div>

      <!-- Sélection du référentiel -->
      <div class="form-group">
        <label>Référentiel <span class="required">*</span></label>
        <select v-model="selectionReferentiel" required>
          <option disabled value="">-- Sélectionnez un référentiel --</option>
          <option v-for="ref in referentiels" :key="ref.idReferentiel" :value="ref.idReferentiel">
            {{ ref.nom }}
          </option>
        </select>
      </div>

      <!-- Sélection de l'action existante -->
      <div class="form-group">
        <label>Action <span class="required">*</span></label>
        <select v-model="selectionAction" required>
          <option disabled value="">-- Sélectionnez une action --</option>
          <option v-for="act in actions" :key="act.idAction" :value="act.idAction">
            {{ act.nom }}
          </option>
        </select>
      </div>

      <!-- Dates de participation -->
      <div class="form-group">
        <label>Date de début de participation <span class="required">*</span></label>
        <input type="date" v-model="dateDebutParticipation" required />
      </div>
      <div class="form-group">
        <label>Date de fin de participation <span class="required">*</span></label>
        <input type="date" v-model="dateFinParticipation" required />
      </div>

      <!-- Description de la participation -->
      <div class="form-group">
        <label>Description de la participation <span class="required">*</span></label>
        <textarea rows="4" v-model="descriptionParticipation" placeholder="Décrivez votre participation..." required></textarea>
      </div>

      <div class="btn-group">
        <button type="submit" class="btn-submit">Envoyer</button>
        <button type="button" class="btn-cancel" @click="handleCancel">Annuler</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

// Importation des classes depuis le dossier 'classe'
import { Etudiant } from '../../classe/Etudiant.js'
import { Semestre } from '../../classe/Semestre.js'
import { Referentiel } from '../../classe/Referentiel.js'
import { Action } from '../../classe/Action.js'

const router = useRouter()

// Variables réactives pour stocker les listes
const etudiants = ref([])
const semestres = ref([])
const referentiels = ref([])
const actions = ref([])

// Variables pour les valeurs du formulaire
const selectionEtudiant = ref('')
const selectionSemestre = ref('')
const selectionReferentiel = ref('')
const selectionAction = ref('')
const dateDebutParticipation = ref('')
const dateFinParticipation = ref('')
const descriptionParticipation = ref('')

// Endpoints API
const ETUDIANTS_URL    = 'http://localhost:8989/api/etudiants'
const SEMESTRES_URL    = 'http://localhost:8989/api/semestres'
const REFERENTIELS_URL = 'http://localhost:8989/api/referentiels'
const ACTIONS_URL      = 'http://localhost:8989/api/actions'
const PARTICIPES_URL   = 'http://localhost:8989/api/participes'

// Chargement des données

function getEtudiants() {
  fetch(ETUDIANTS_URL, { method: "GET" })
    .then(response => response.json())
    .then(dataJSON => {
      etudiants.value = dataJSON.map(item =>
        new Etudiant(item.idEtudiant, item.nom, item.prenom, item.email, item.promotion)
      )
    })
    .catch(error => console.log(error))
}

function getSemestres() {
  fetch(SEMESTRES_URL, { method: "GET" })
    .then(response => response.json())
    .then(dataJSON => {
      semestres.value = dataJSON.map(item =>
        new Semestre(item.idSemestre, item.nbSemestre, item.date)
      )
    })
    .catch(error => console.log(error))
}

function getReferentiels() {
  fetch(REFERENTIELS_URL, { method: "GET" })
    .then(response => response.json())
    .then(dataJSON => {
      referentiels.value = dataJSON.map(item =>
        new Referentiel(item.idReferentiel, item.nom, item.description)
      )
    })
    .catch(error => console.log(error))
}

function getActions() {
  fetch(ACTIONS_URL, { method: "GET" })
    .then(response => response.json())
    .then(dataJSON => {
      actions.value = dataJSON.map(item =>
        new Action(item.idAction, item.nom, item.descriptionAction, item.dateDebutAction, item.dateFinAction)
      )
    })
    .catch(error => console.log(error))
}

onMounted(() => {
  getEtudiants()
  getSemestres()
  getReferentiels()
  getActions()
})

// Gestion de la soumission du formulaire
function submitParticipation() {
  const participationPayload = {
    etudiant: { idEtudiant: Number(selectionEtudiant.value) },
    action: { idAction: Number(selectionAction.value) },
    semestre: { idSemestre: Number(selectionSemestre.value) },
    dateDebutParticipation: dateDebutParticipation.value,
    dateFinParticipation: dateFinParticipation.value,
    descriptionParticipation: descriptionParticipation.value,
    totalPoints: null,
    nbParticipation: null
  }

  fetch(PARTICIPES_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(participationPayload)
  })
    .then(response => response.json())
    .then(() => {
      alert("Participation créée avec succès !");
      router.push("/AcceuilEtudiantView");
    })
    .catch(error => {
      console.log(error);
      alert("Une erreur est survenue lors de l’envoi de la fiche.");
    })
}

function handleCancel() {
  router.push('/')
}
</script>

<style scoped>
.fiche-form-container {
  max-width: 700px; /* largeur réduite */
  margin: 40px auto; /* centré horizontalement avec une marge supérieure et inférieure */
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
}

.fiche-form-container h2 {
  color: #6C3EA9;
  margin-bottom: 1rem;
  border-bottom: 1px solid #6C3EA9;
  padding-bottom: 0.5rem;
}

.fiche-form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.4rem;
  font-weight: bold;
  color: #333;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
  box-sizing: border-box;
  font: inherit;
}

.required {
  color: red;
}

.btn-group {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.btn-submit {
  padding: 10px 20px;
  background-color: #d32f2f;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-submit:hover {
  background-color: #c12727;
}

.btn-cancel {
  padding: 10px 20px;
  background-color: #ff9800;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-cancel:hover {
  background-color: #e68900;
}
</style>
