import hashlib
import re

from better_profanity import profanity
from faker import Faker
from flask import Request, Response
from markupsafe import escape

fake = Faker()


def validate_name(name: str):
    if name is None or len(name) < 1:
        return None
    if len(name) < 4 or len(name) > 16:
        return None
    if not re.match("^[a-zA-Z0-9_-]+$", name):
        return None
    if profanity.contains_profanity(name):
        return None
    return escape(name)


def generate_username():
    username = ""
    for i in range(10):
        username = fake.user_name()
        if 4 <= len(username) <= 16:
            break

    return username


def get_hashed_ip(ip_addr: str):
    return hashlib.sha256(ip_addr.encode('utf-8')).hexdigest()


def get_primary_identifier(request: Request):
    forwarded_for = request.headers.get('X-Forwarded-For', None)
    if forwarded_for:
        # X-Forwarded-For can contain a list of IPs. The first one is the client IP.
        client_ip = forwarded_for.split(',')[0].strip()
    else:
        client_ip = request.remote_addr
    return get_hashed_ip(client_ip)


def create_uid_cookie_response(response: Response, value: str, max_age=60 * 60 * 24):
    response.set_cookie("uid", value, max_age=max_age, path="/", samesite="None", httponly=True, secure=True)
    return response
