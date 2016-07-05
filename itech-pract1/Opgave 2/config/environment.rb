# Load the Rails application.
require File.expand_path('../application', __FILE__)

# Initialize the Rails application.
Eventapp::Application.initialize!

Date::DATE_FORMATS[:default] = "%m-%d-%Y"
Time::DATE_FORMATS[:default] = "%m-%d-%Y %H:%M"
