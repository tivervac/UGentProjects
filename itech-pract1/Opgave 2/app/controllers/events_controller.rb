class EventsController < ApplicationController
  def new
    @event = Event.new
  end

  def index
    @events = Event.all
  end

  def create
    @event = Event.new(event_params)

    respond_to do |format|
      if @event.save
        format.html { redirect_to @event }
        format.json { render json: event_url(@event), status: :created }
      else
        format.html { render 'new' }
        format.json { render json: @event.errors, status: :bad_request }
      end
    end
  end

  def show
    @event = Event.find(params[:id])
  end

  def edit
    @event = Event.find(params[:id])
  end

  def update
    @event = Event.find(params[:id])
    respond_to do |format|
      if @event.update(params[:event].permit(:title, :text, :start, :end))
        format.html { redirect_to @event }
        format.json { render json: event_url(@event), status: :accepted }
      else
        format.html { render 'edit' }
        format.json { render json: @event.errors, status: :bad_request }
      end
    end
  end

  def destroy
    @event = Event.find(params[:id])
    @event.destroy

    respond_to do |format|
      format.html { redirect_to events_path }
      format.json { render json: event_url(), status: :accepted }
    end
  end

  private

  def event_params
    params.require(:event).permit(:title, :text, :start, :end)
  end
end
