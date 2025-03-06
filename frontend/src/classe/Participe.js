export class Participe {
  constructor(etudiant, action, semestre, dateDebutParticipation, dateFinParticipation, descriptionParticipation, totalPoints = null, nbParticipation = null) {
    this._etudiant = etudiant; // Instance de Etudiant
    this._action = action;     // Instance de Action
    this._semestre = semestre; // Instance de Semestre
    this._dateDebutParticipation = dateDebutParticipation;
    this._dateFinParticipation = dateFinParticipation;
    this._descriptionParticipation = descriptionParticipation;
    this._totalPoints = totalPoints;
    this._nbParticipation = nbParticipation;
  }

  get etudiant() { return this._etudiant; }
  set etudiant(value) { this._etudiant = value; }

  get action() { return this._action; }
  set action(value) { this._action = value; }

  get semestre() { return this._semestre; }
  set semestre(value) { this._semestre = value; }

  get dateDebutParticipation() { return this._dateDebutParticipation; }
  set dateDebutParticipation(value) { this._dateDebutParticipation = value; }

  get dateFinParticipation() { return this._dateFinParticipation; }
  set dateFinParticipation(value) { this._dateFinParticipation = value; }

  get descriptionParticipation() { return this._descriptionParticipation; }
  set descriptionParticipation(value) { this._descriptionParticipation = value; }

  get totalPoints() { return this._totalPoints; }
  set totalPoints(value) { this._totalPoints = value; }

  get nbParticipation() { return this._nbParticipation; }
  set nbParticipation(value) { this._nbParticipation = value; }
}
