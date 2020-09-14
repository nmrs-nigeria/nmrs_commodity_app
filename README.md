# The NMRS Inventory Module
The inventory module is an [OpenMRS] [5] module to provide simple inventory management from within OpenMRS. It supports managing stockrooms and items, and the creation of stock operations to move item stock in to, within, and out of your facility.

## Project Details
You will find our installation and user documentation for this module on the OpenMRS wiki site under the OpenHMIS Inventory Module once that documentation is complete.  We welcome any feedback and/or bug reports on our [bug tracking system] [2].  

Questions or comments? Please use the issue tracker


## Repository Information
This repository follows the [git flow] [3] branching model.  The current released build is always at the HEAD of the master branch while our current development version is at the HEAD of the develop branch.  A tag is created for each released version using the version number as the name; for example v1.5.0.

[0]: https://wiki.openmrs.org/display/docs/OpenHMIS+Modules
[1]: http://openmrs.openhmisafrica.org
[2]: http://issues.openhmisafrica.org
[3]: https://github.com/nvie/gitflow
[4]: http://www.hipchat.com/gHNSPJwzw
[5]: http://www.openmrs.org

[![Analytics](https://ga-beacon.appspot.com/UA-46919671-1/openmrs-module-openhmis.inventory/readme)](https://github.com/igrigorik/ga-beacon)


## Developer Installation Guide
1. Clone the repo
2. Navigate to the omod directory
3. run the command below
$ mvn install:install-file -Dfile=lib/nmrs-signer-obs.jar -DgroupId=com.morris.NDRSigner -DartifactId=nmrs-encrypt-obs -Dversion=1.0 -Dpackaging=jar

4. run mvn clean install.
