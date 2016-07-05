class Event < ActiveRecord::Base
  has_many :comments, :dependent => :destroy
  has_many :participants, dependent: :delete_all
  has_many :people, :through => :participants
  validates :title, presence: true, length: {minimum: 1}
  validates :text, presence: true, length: {minimum: 1}
  validates :start, :end, presence: true
end
