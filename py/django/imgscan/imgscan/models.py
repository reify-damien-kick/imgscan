from django.db import models


def imgpath(model, filename):
    return F'imgscan/{filename}'


class Image(models.Model):
    # The spec calls for a field named 'objects' but this collides
    # with a Python field of the same name. We'll rename the field in
    # the serializer
    dbobjects = models.Manager()

    id = models.BigAutoField(primary_key=True)
    imgfile = models.FileField(upload_to=imgpath, unique=True)
    detect = models.BooleanField(default=True)

    objects = models.ManyToManyField('ImgObject')


class ImgObject(models.Model):
    label = models.CharField(primary_key=True, max_length=256)

    def __str__(self):
        return self.label
