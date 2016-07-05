# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20140311193347) do

  create_table "comments", force: true do |t|
    t.text     "body"
    t.integer  "person_id"
    t.integer  "event_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "comments", ["event_id"], name: "index_comments_on_event_id"
  add_index "comments", ["person_id"], name: "index_comments_on_person_id"

  create_table "events", force: true do |t|
    t.string   "title"
    t.text     "text"
    t.datetime "start"
    t.datetime "end"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "participants", id: false, force: true do |t|
    t.integer  "event_id"
    t.integer  "person_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "participants", ["person_id", "event_id"], name: "index_participants_on_person_id_and_event_id"

  create_table "people", force: true do |t|
    t.string   "name"
    t.string   "email"
    t.date     "dateofbirth"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

end
