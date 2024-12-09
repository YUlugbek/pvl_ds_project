from fastapi import FastAPI, HTTPException, Form, Request, Depends
from fastapi.responses import RedirectResponse
from fastapi.templating import Jinja2Templates
from fastapi.staticfiles import StaticFiles
from starlette.status import HTTP_303_SEE_OTHER
import requests
import os

# Initialize FastAPI app
app = FastAPI(title="Christmas Shopping Items API", version="v0")

# Read the environment variable
SPRING_BOOT_API_URL = os.getenv("SPRING_BOOT_API_URL", "http://localhost:8080/api/shoppingItems")

# Set up templates directory
templates = Jinja2Templates(directory="templates")

# Mount static files if needed (for CSS, JS, etc.)
#app.mount("/static", StaticFiles(directory="static"), name="static")


# Home Page: Display all shopping items
@app.get("/")
def index(request: Request):
    response = requests.get(SPRING_BOOT_API_URL)
    if response.status_code == 200:
        items = response.json()
    else:
        items = []
        error = "Unable to fetch items from the API."
    return templates.TemplateResponse("index.html", {"request": request, "items": items})


# Add a new shopping item
@app.get("/add")
def add_item_form(request: Request):
    return templates.TemplateResponse("add_item.html", {"request": request})


@app.post("/add")
def add_item(name: str = Form(...), amount: int = Form(...)):
    payload = {"name": name, "amount": amount}
    response = requests.post(SPRING_BOOT_API_URL, json=payload)
    if response.status_code in [200, 201]:
        return RedirectResponse(url="/", status_code=HTTP_303_SEE_OTHER)
    raise HTTPException(status_code=400, detail="Failed to add item.")


# Update an existing shopping item
@app.get("/update/{name}")
def update_item_form(request: Request, name: str):
    return templates.TemplateResponse("update_item.html", {"request": request, "name": name})


@app.post("/update/{name}")
def update_item(name: str, amount: int = Form(...)):
    payload = {"name": name, "amount": amount}
    response = requests.put(f"{SPRING_BOOT_API_URL}/{name}", json=payload)
    if response.status_code == 200:
        return RedirectResponse(url="/", status_code=HTTP_303_SEE_OTHER)
    raise HTTPException(status_code=400, detail="Failed to update item.")


# Delete a shopping item
@app.get("/delete/{name}")
def delete_item(name: str):
    response = requests.delete(f"{SPRING_BOOT_API_URL}/{name}")
    if response.status_code == 204:
        return RedirectResponse(url="/", status_code=HTTP_303_SEE_OTHER)
    raise HTTPException(status_code=400, detail="Failed to delete item.")
