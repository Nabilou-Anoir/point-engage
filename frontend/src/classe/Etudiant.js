export class Etudiant {
  constructor(idEtudiant, nom, prenom, email, promotion) {
    this._idEtudiant = idEtudiant;
    this._nom = nom;
    this._prenom = prenom;
    this._email = email;
    this._promotion = promotion;
  }

  // Getters and Setters
  get idEtudiant() { return this._idEtudiant; }
  set idEtudiant(value) { this._idEtudiant = value; }

  get nom() { return this._nom; }
  set nom(value) { this._nom = value; }

  get prenom() { return this._prenom; }
  set prenom(value) { this._prenom = value; }

  get email() { return this._email; }
  set email(value) { this._email = value; }

  get promotion() { return this._promotion; }
  set promotion(value) { this._promotion = value; }
}
