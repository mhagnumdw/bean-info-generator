#!/bin/bash

set -e

PS4="$(tput setaf 14)+ $(tput sgr0)"

echo

cat << 'EOF'
This script will perform the following actions:
 - Create a new release version.
 - Set the next development version.
 - Modify and commit the pom.xml file.
 - Push the changes to the remote repository.
 - Sign artifacts using the GPG key specified in pom.xml.
 - Publish artifacts to the Maven Central Repository.
EOF

log_i() {
    echo "$(tput setaf 2)$1$(tput sgr0)"
}

log_e() {
    echo "$(tput setaf 1)[ERROR] $1$(tput sgr0)"
}

DEFAULT_BRANCH="main"
if [ "$DEFAULT_BRANCH" != "$(git branch --show-current)" ]; then
    log_e "This script must be run on the '$DEFAULT_BRANCH' branch."
    exit 1
fi

if [ ! -f "./pom.xml" ]; then
    log_e "This script must be run from the project root directory."
    exit 1
fi

if [ -n "$(git fetch --dry-run)" ]; then
    log_e "Local repository is not up-to-date. Please run 'git pull'."
    exit 1
fi

echo

log_i "Verifying user and email for the commit"
echo -n 'user.name : '
git config user.name
echo -n 'user.email: '
git config user.email

echo
read -r -p "Is this information correct? Press Enter to continue, or Ctrl+C to abort"
echo

log_i "Current pom.xml and Java versions"
./mvnw -V help:evaluate -Dexpression=project.version -q -DforceStdout
echo
read -r -p "Is this information correct? Press Enter to continue, or Ctrl+C to abort"
echo

log_i "Enter credentials for deployment to Maven Central Repository (OSSRH)"
echo "(You can get these from https://central.sonatype.com/account)"
read -s -r -p "Username (OSSRH_USER): " OSSRH_USER
echo
read -s -r -p "Password (OSSRH_PASS): " OSSRH_PASS
echo
export OSSRH_USER
export OSSRH_PASS
echo

log_i "Enter GPG passphrase for key $(grep -oPm1 '(?<=<keyname>)[^<]+' pom.xml)"
read -s -r -p "GPG Passphrase: " MAVEN_GPG_PASSPHRASE
echo
export MAVEN_GPG_PASSPHRASE
echo

log_i "Generating release..."
set -x
./mvnw clean release:clean release:prepare release:perform --settings .mvn/settings.xml
set +x

echo

log_i "Showing the last 7 commits"
git log --pretty="%C(Yellow)%h  %C(reset)%ad (%C(Green)%cr%C(reset))%x09 %C(Cyan)%an (%ae): %C(reset)%s" -7

echo

log_i "Done."
