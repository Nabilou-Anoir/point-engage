<template>
  <div class="modal-overlay" v-if="visible" @click.self="close">
    <div class="modal-content">
      <h2>Suivi de la Fiche</h2>

      <div v-if="participation" class="timeline">
        <div class="timeline-step done">
          <div class="icon">📥</div>
          <div class="label">Fiche soumise</div>
        </div>

        <div class="timeline-step" :class="{ active: participation.statut === null, done: participation.statut !== null }">
          <div class="icon">🔍</div>
          <div class="label">Évaluation en cours</div>
        </div>

        <div v-if="participation.statut === true" class="timeline-step done">
          <div class="icon">✅</div>
          <div class="label">Validée</div>
        </div>

        <div v-if="participation.statut === false" class="timeline-step error">
          <div class="icon">❌</div>
          <div class="label">Refusée</div>
        </div>

        <div v-if="participation.totalPoints && participation.totalPoints > 0" class="timeline-step done">
          <div class="icon">🏅</div>
          <div class="label">{{ participation.totalPoints }} points attribués</div>
        </div>
      </div>

      <div v-else class="no-data">
        <p>Aucune participation trouvée.</p>
      </div>

      <button class="modal-close-button" @click="close">Fermer</button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  visible: Boolean,
  participationData: Object
});

const emit = defineEmits(['close']);

const participation = ref(null);

watch(() => props.participationData, (newVal) => {
  participation.value = newVal;
});

const close = () => emit('close');
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0; left: 0; width: 100%; height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(5px);
}

.modal-content {
  background: white;
  padding: 40px;
  border-radius: 16px;
  max-width: 600px;
  width: 90%;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  text-align: center;
}

.timeline {
  margin-top: 30px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.timeline-step {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 1.1rem;
  padding: 15px;
  border-radius: 10px;
  background: #f4f4f9;
  transition: 0.3s ease;
  justify-content: flex-start;
}

.timeline-step .icon {
  font-size: 1.8rem;
}

.timeline-step.active {
  border-left: 5px solid #6A3FA0;
  background: #f2ebff;
  font-weight: 600;
}

.timeline-step.done {
  background: #e0f7e9;
  border-left: 5px solid #2ecc71;
  color: #2c3e50;
}

.timeline-step.error {
  background: #ffe0e0;
  border-left: 5px solid #e74c3c;
  color: #c0392b;
}

.modal-close-button {
  margin-top: 30px;
  padding: 12px 24px;
  background: #6A3FA0;
  color: white;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.modal-close-button:hover {
  background-color: #5a2f8f;
}
</style>
