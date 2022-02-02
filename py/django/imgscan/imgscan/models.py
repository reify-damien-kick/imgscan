from django.db import models


def imgpath(model, filename):
    return F'imgscan/{model.user.id}/{filename}'


class Image(models.Model):
    id = models.BigAutoField(primary_key=True)
    imgfile = models.FileField(upload_to=imgpath)
    detect = models.BooleanField(default=True)
    imgobjects = models.ForeignKey(
        'ImgObject', on_delete=models.CASCADE, to_field='label',
        null=True)


class ImgObject(models.Model):
    id = models.BigAutoField(primary_key=True)
    label = models.CharField(unique=True, max_length=256)
