class PeopleController < ApplicationController
  def new
    @person = Person.new
  end

  def create
    @person = Person.new(person_params)

    respond_to do |format|
      if @person.save
        format.html { redirect_to @person }
        format.json { render json: person_url(@person), status: :created }
      else
        format.html { render 'new' }
        format.json { render json: @person.errors, status: :bad_request }
      end
    end
  end

  def show
    @person = Person.find(params[:id])
  end

  def index
    @people = Person.all
  end

  def edit
    @person = Person.find(params[:id])
  end

  def update
    @person = Person.find(params[:id])
    respond_to do |format|
      if @person.update(person_params)
        format.html { redirect_to @person }
        format.json { render json: person_url(@person), status: :accepted }
      else
        format.html { render 'edit' }
        format.json { render json: @person.errors, status: :bad_request }
      end
    end
  end

  def destroy
    @person = Person.find(params[:id])
    @person.destroy

    respond_to do |format|
      format.html { redirect_to people_path }
      format.json { render json: people_url(), status: :accepted }
    end
  end

  private

  def person_params
    params.require(:person).permit(:name, :email, :dateofbirth)
  end
end
