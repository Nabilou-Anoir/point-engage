export class Referent {
  constructor(idReferent, nom, prenom, email) {
    this._idReferent = idReferent;
    this._nom = nom;
    this._prenom = prenom;
    this._email = email;
  }

  get idReferent() { return this._idReferent; }
  set idReferent(value) { this._idReferent = value; }

  get nom() { return this._nom; }
  set nom(value) { this._nom = value; }

  get prenom() { return this._prenom; }
  set prenom(value) { this._prenom = value; }

  get email() { return this._email; }
  set email(value) { this._email = value; }
}
