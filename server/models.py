import uuid

from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()


class User(db.Model):
    id = db.Column(db.String(36), primary_key=True, default=lambda: str(uuid.uuid4()))
    name = db.Column(db.String(32), nullable=False)
    score = db.Column(db.Integer, nullable=False, default=0)
    primary_identifier = db.Column(db.String(256), nullable=False)
    fingerprint = db.Column(db.String(256), nullable=False)

    def to_dict(self):
        return {"id": self.id,
                "name": self.name,
                "score": self.score,
                "primary_identifier": self.primary_identifier,
                "fingerprint": self.fingerprint}

    def to_simple_dict(self):
        return {"id": self.id,
                "name": self.name,
                "score": self.score,
                "has_fingerprint": len(self.fingerprint) > 0}
