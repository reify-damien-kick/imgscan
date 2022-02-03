from django.db import models


def imgpath(model, filename):
    return F'imgscan/{filename}'


class Image(models.Model):
    # The spec calls for a field named 'objects'; rename the default
    # manager to avoid colliding.
    dbobjects = models.Manager()

    id = models.BigAutoField(primary_key=True)
    imgfile = models.FileField(upload_to=imgpath, unique=True)
    detect = models.BooleanField(default=True)
    scanned = models.DateTimeField(null=True)

    objects = models.ManyToManyField('ImgObject')


class ImgObject(models.Model):
    label = models.CharField(primary_key=True, max_length=256)

    def __str__(self):
        return self.label
