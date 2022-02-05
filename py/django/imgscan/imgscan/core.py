import io

from django.db import transaction
from django.utils import timezone
from google.cloud import vision

from imgscan import models


def labels(filename):
    client = vision.ImageAnnotatorClient()

    with io.open(filename, 'rb') as infile:
        content = infile.read()

    image = vision.Image(content=content)
    response = client.label_detection(image=image)
    x_labels = response.label_annotations

    return [label.description for label in x_labels]


def images_to_scan():
    mgr = models.Image.dbobjects
    qry = mgr.filter(detect=True, scanned__isnull=True)
    return qry.all()


def update_images():
    for image in images_to_scan():
        update_image_labels(image)


def safe_update_images():
    with transaction.atomic():
        update_images()


def update_image_labels(image):
    x_labels = labels(image.imgfile.file.name)
    for label in x_labels:
        imgobject = models.ImgObject.objects.get_or_create(label=label)
        imgobject = imgobject[0]
        image.objects.add(imgobject)
    image.scanned = timezone.now()
    image.save()
    return image


def safe_update_image_labels(image):
    with transaction.atomic():
        return update_image_labels(image)
