class Event < ActiveRecord::Base
  has_many :comments, :dependent => :destroy
  has_many :participants, dependent: :delete_all
  has_many :people, :through => :participants
  validates :title, presence: true, length: {minimum: 1}  #title has to be present
  validates :text, presence: true, length: {minimum: 1}   #text has to be present
end
