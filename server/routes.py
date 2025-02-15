import logging

from flask import Blueprint, request, jsonify, make_response
from sqlalchemy import desc

from models import db, User
from util import validate_name, generate_username, get_primary_identifier, create_uid_cookie_response

api = Blueprint("api", __name__)
logger = logging.getLogger(__name__)


@api.route("/users", methods=["GET"])
def get_users():
    users = User.query.order_by(desc(User.score)).all()
    return jsonify({"players": [user.to_simple_dict() for user in users]})


@api.route("/users/current", methods=["POST"])
def current_user():
    uid = request.cookies.get("uid", None)

    if uid is not None:
        user = User.query.get(uid)
        if user is not None:
            return create_uid_cookie_response(make_response(jsonify(user.to_simple_dict()), 200), uid)

    data = request.get_json(silent=True)

    if data is None:
        return jsonify({"error": "No input data provided"}), 400

    userid = data.get("id", None)
    if userid is not None:
        user = User.query.get(userid)
        if user is not None:
            return create_uid_cookie_response(make_response(jsonify(user.to_simple_dict()), 200), userid)

    primary_identifier = get_primary_identifier(request)
    if primary_identifier is not None:
        users = User.query.filter_by(primary_identifier=primary_identifier).all()

        if len(users) < 1:
            return jsonify({"error": "User not found."}), 404
        elif len(users) == 1:
            return create_uid_cookie_response(make_response(jsonify(users[0].to_simple_dict()), 200), users[0].id)
        else:
            fingerprint = data.get("fingerprint", None)

            if fingerprint is not None:
                fingerprint_users = [user for user in users if user.fingerprint == fingerprint]
                if len(fingerprint_users) == 1:
                    return create_uid_cookie_response(
                        make_response(jsonify(fingerprint_users[0].to_simple_dict()), 200), fingerprint_users[0])
                else:
                    if (len(fingerprint_users)) > 1:
                        # Should raise concerns about improving fingerprinting.
                        logger.warning("Multiple users with identical fingerprint found.")

    return jsonify({"error": "User not found."}), 404


@api.route("/users", methods=["POST"])
def create_user():
    data = request.get_json(silent=True)
    if data is None:
        return jsonify({"error": "No input data provided"}), 400

    name = validate_name(data.get("name", None))
    if name is None:
        name = generate_username()

    primary_identifier = get_primary_identifier(request)

    fingerprint = data.get("fingerprint", "")

    new_user = User(name=name,
                    score=data.get("score", 0),
                    primary_identifier=primary_identifier,
                    fingerprint=fingerprint)

    db.session.add(new_user)
    db.session.commit()

    return create_uid_cookie_response(make_response(jsonify(new_user.to_simple_dict()), 201), new_user.id)


@api.route("/users/current", methods=["PUT"])
def update_current_user():
    user = None

    uid = request.cookies.get("uid", None)
    if uid is not None:
        user = User.query.get(uid)

    data = request.get_json(silent=True)

    if data is None:
        return jsonify({"error": "No input data provided."}), 400

    if user is None:
        userid = data.get("id", None)
        if userid is not None:
            user = User.query.get(userid)

    if user is None:
        return jsonify({"error": "User not found."}), 404

    name = data.get("name", None)
    if name is not None:
        name = validate_name(name)
        if name is not None:
            user.name = name

    score = data.get("score", user.score)
    if score >= 0:
        user.score = score

    fingerprint = data.get("fingerprint", "")
    if len(fingerprint) > 0:
        user.fingerprint = fingerprint

    db.session.commit()

    return create_uid_cookie_response(make_response(jsonify(user.to_simple_dict()), 200), user.id)
