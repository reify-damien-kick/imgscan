from django.core.management.base import BaseCommand


class Command(BaseCommand):
    help = 'Uses google-cloud-vision to scan for objects in images'

    def handle(self, *args, **kwargs):
        pass
  
