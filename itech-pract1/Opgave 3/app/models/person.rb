class Person < ActiveRecord::Base
  has_many :participants, dependent: :delete_all
  has_many :events, :through => :participants
  has_many :comments, :dependent => :destroy

  validates :name, presence: true, length: {minimum: 1}
  validates :email, presence: true, format: { with: /.+@[A-Za-z0-9.-]+\.[A-Za-z]+\z/, message: "invalid" }
  validates :dateofbirth, presence: true
end
